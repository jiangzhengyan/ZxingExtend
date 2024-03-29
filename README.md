
## 长按识别二维码
基于下zxing实现的长按二维码识别
### **界面效果**
![image](https://img-blog.csdnimg.cn/direct/3a00671b179143d6b70a50a13cd76349.gif =220x)

### **特点**

1. **长按(其他触发方式)识别指定区域的二维码**
2. **自定义识别区域**
3. **结果点的颜色,大小等可以灵活定义**

### **属性方法和使用**    
 <br>
1,将QrImageResultPointView和所需要识别的区域位置和大小保持一致  
<br>    


```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/rl_root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="fitXY"
        android:src="@mipmap/code" />

    <com.smart.zxingextend.QrImageResultPointView
        android:id="@+id/qr_point"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="gone"
        app:pv_point_color="#CC22CE6A"
        app:pv_point_radius_corner="12dp"
        app:pv_point_stroke_color="#FFFFFF"
        app:pv_point_stroke_width="4dp"
        app:pv_point_w_h="40dp" />

</RelativeLayout>
```
<br> 
2,触发(长按)QrImageResultPointView的方法decodeQrcode(View viewCode),添加点击事件。
onPointClick(String result)返回用户选中的二维码点位内容
<br> 

```java
rl_root.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        qr_point.decodeQrcode(rl_root);
        return true;
    }
});
qr_point.setOnResultPointClickListener(new QrImageResultPointView.OnResultPointClickListener() {
    @Override
    public void onPointClick(String result) {
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancle() {

    }
});
```

### **demo地址**
附上github上demo地址,有需要的可以下载指正。
   [demo地址下载](https://github.com/jiangzhengyan/ZxingExtend)
    

