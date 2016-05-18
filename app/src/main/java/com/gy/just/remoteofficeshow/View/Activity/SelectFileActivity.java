package com.gy.just.remoteofficeshow.View.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gy.just.remoteofficeshow.R;
import com.gy.just.remoteofficeshow.Service.Flag.UploadFlag;
import com.gy.myframework.IOC.Core.annotation.InjectCollectionView;
import com.gy.myframework.IOC.Service.event.impl.EventPoster;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ContentView;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.OnItemClick;
import com.gy.myframework.IOC.UI.view.viewinject.annotation.ViewInject;
import com.gy.myframework.IOC.UI.view.viewinject.impl.ViewInjectAll;
import com.gy.myframework.MVP.Presenter.IPresenterCallBack;
import com.gy.myframework.MVP.Presenter.Presenter;
import com.gy.myframework.MVP.View.context.activity.BaseAppCompactActivity;
import com.gy.myframework.UI.view.collectionview.viewholder.IViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_detail)
public class SelectFileActivity extends BaseAppCompactActivity{

    @ViewInject(R.id.files)
    private ListView listView;
    private List<String> item, paths,filelist;
    private String rootpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private SparseArray<Bitmap> iconCaches;
    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle("选择文件");
        init();
    }

    private void init(){
        filelist = new ArrayList<String>();
        item = new ArrayList<String>();
        paths = new ArrayList<String>();
        iconCaches = new SparseArray<Bitmap>();
        getdir(rootpath);
    }

    @Override
    protected IPresenterCallBack getPresent() {
        return Presenter.getCurPresenter();
    }

    @InjectCollectionView(listlayout = R.id.files,itemlayout = R.layout.filelistlayout)
    public void filesShow(IViewHolder holder, int position){
        File file = new File(paths.get(position));
        if (item.get(position).toString().equals("back")) {
            holder.setText(R.id.filename,"返回上一层......");
            holder.setImgRec(R.id.icon, R.drawable.folder);
        } else {
            holder.setText(R.id.filename,item.get(position));
            if (file.isDirectory()) {
                holder.setImgRec(R.id.icon, R.drawable.folder);
            } else {
                holder.setImgBitmap(R.id.icon, iconselect(getMIMEType(file)));
            }
        }
    }

    @OnItemClick(R.id.files)
    public void onFileItemClicked(AdapterView<?> parent, View view, int position, long id){
        File templefile = new File(paths.get(position));
        if (templefile.isDirectory()) {
            try {
                getdir(paths.get(position));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                getdir(rootpath);
                Toast.makeText(this, "未获得权限无法查看",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            if (getMIMEType(templefile).equals("text/*")){
                collapsingToolbar.setTitle(templefile.getName());
                EventPoster.getInstance().broadcast(templefile);
            }else {
                Toast.makeText(this, "非文档文件",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkin(View view) {
        Snackbar.make(view, "准备上传....", Snackbar.LENGTH_SHORT).show();
        EventPoster.getInstance().broadcast(new UploadFlag());
        finish();
    }

    private void ListViewHeightFix(){
        int height = 0;
        Adapter adapter = ViewInjectAll.getInstance().getAdapter(this,"filesShow");
        for (int i = 0;i < adapter.getCount();i++){
            View listitem = adapter.getView(i,null,listView);
            listitem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listitem.measure(0, 0);
            height += listitem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void getdir(String filepath) {
        item.clear();
        paths.clear();
        File file = new File(filepath);
        File[] files = file.listFiles();
        if (!filepath.equals(rootpath)) {
            item.add("back");
            paths.add(file.getParent());
        }
        for (int i = 0; i < files.length; i++) {
            File tempfile = files[i];
            item.add(tempfile.getName());
            paths.add(tempfile.getPath());
        }
        if (filepath == rootpath)
            ViewInjectAll.getInstance().showList(this,"filesShow",item);
        else
            ViewInjectAll.getInstance().refreshList(this,"filesShow");
        ListViewHeightFix();
    }

    private Bitmap getIconBitmap(int id){
        Bitmap bitmap = iconCaches.get(id);
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(),id);
            iconCaches.put(id,bitmap);
        }
        return bitmap;
    }

    private String getMIMEType(File f) {
        String filetype = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            filetype = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4") || end.equals("rmvb")
                || end.equals("rm") || end.equals("avi") || end.equals("mkv")) {
            filetype = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            filetype = "image/*";
        } else if (end.equals("docx") || end.equals("xlsx")
                || end.equals("doc") || end.equals("xls")|| end.equals("ppt") || end.equals("pptx")) {
            filetype = "text/*";

        } else if (end.equals("apk")) {
            filetype = "application/vnd.android.package-archive";
        }else if (end.equals("zip")||end.equals("rar")||end.equals("7z")||end.equals("tar")) {
            filetype = "application/*";
        } else {
            filetype = "*/*";
        }

        return filetype;
    }

    private Bitmap iconselect(String type) {
        Bitmap bitmap = null;
        switch (type) {
            case "audio/*":
                bitmap = getIconBitmap(R.drawable.music);
                break;
            case "video/*":
                bitmap = getIconBitmap(R.drawable.video_v2);
                break;
            case "image/*":
                bitmap = getIconBitmap(R.drawable.image);
                break;
            case "text/*":
                bitmap = getIconBitmap(R.drawable.text);
                break;
            case "application/vnd.android.package-archive":
                bitmap = getIconBitmap(R.drawable.ic_launcher);
                break;
            case "application/*":
                bitmap = getIconBitmap(R.drawable.archive_orange);
                break;
            default:
                bitmap = getIconBitmap(R.drawable.file);
                break;
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    Toast.makeText(this,"Destory",Toast.LENGTH_LONG).show();
    }

}
