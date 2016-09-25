# DiVisionEditText
可自定义的分割型输入框 如银行卡卡号输入
自定义属性： 
      
    placeHolder：占位分隔符
    placeIndex： 分割位置
    
    placeIndex必须配合placeHolder 单独使用不起作用
    
    直接在布局文件中使用：
   
    <com.jamesp1949.divisionlib.DivisionEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:placeHolder="-"
        app:placeIndex="5"
        android:hint="随便输入点什么"
        android:textColor="@android:color/holo_blue_bright"/>
