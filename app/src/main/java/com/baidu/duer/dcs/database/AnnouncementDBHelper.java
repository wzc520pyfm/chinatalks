package com.baidu.duer.dcs.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baidu.duer.dcs.bean.AnnInfo;

import java.util.ArrayList;
/***************************************************************************************
 * 类:                Ann数据库帮助类
 * 用途:              实现数据存储--存储公告信息
 *===================================================================================== */
public class AnnouncementDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="ann.db";//数据库的名称
    private static final int DB_VERSION=1;//数据库的版本号
    private static AnnouncementDBHelper annHelper=null;//数据库帮助器的实例
    private SQLiteDatabase annDB=null;//数据库的实例
    public static final String TABLE_NAME="ann_info";//表的名称

    public AnnouncementDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    private AnnouncementDBHelper(Context context,int version){
        super(context,DB_NAME,null,version);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static AnnouncementDBHelper getInstance(Context context,int version){
        if(version>0&&annHelper==null){
            annHelper=new AnnouncementDBHelper(context,version);
        }else if(annHelper==null){
            annHelper=new AnnouncementDBHelper(context);
        }
        return annHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(annDB==null||!annDB.isOpen()){
            annDB=annHelper.getReadableDatabase();
        }
        return annDB;
    }

    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(annDB==null||!annDB.isOpen()){
            annDB=annHelper.getWritableDatabase();
        }
        return annDB;
    }

    //关闭数据库连接
    public void closeLink(){
        if(annDB!=null&&annDB.isOpen()){
            annDB.close();
            annDB=null;
        }
    }

    //创建数据库,执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db){
        String drop_sql="DROP TABLE IF EXISTS "+TABLE_NAME+";";
        db.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("
                +"_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                +"title VARCHAR NOT NULL,"+"release_time VARCHAR NOT NULL,"
                +"main_text VARCHAR NOT NULL"+");";
        db.execSQL(create_sql);
    }

    //修改数据库,执行表结构变更语句
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}

    //根据指定条件删除表记录
    public int delete(String condition){
        //执行删除记录动作,该语句返回删除记录的数目
        return annDB.delete(TABLE_NAME,condition,null);
    }

    //删除该表的所有记录
    public int deleteAll(){
        //执行删除记录动作,该语句返回删除记录的数目
        return annDB.delete(TABLE_NAME,"1=1",null);
    }

    //往表中添加一条记录
    public long insert(AnnInfo info){
        ArrayList<AnnInfo> infoArray=new ArrayList<AnnInfo>();
        infoArray.add(info);
        return insert(infoArray);//这里调用了下面添加多条记录的函数
    }

    //往表中添加多条记录
    public long insert(ArrayList<AnnInfo> infoArray){
        long result=-1;
        for(int i=0;i<infoArray.size();i++){
            AnnInfo info=infoArray.get(i);
            ArrayList<AnnInfo> tempArray=new ArrayList<AnnInfo>();

            //根据release_time来判断公共是否已存入本地数据库,如果已存入,则不
            //进行任何操作,如果没有存入,则插入新纪录
            if(info.release_time!=null&&info.release_time.length()>0){
                String condition=String.format("release_time='%s'",info.release_time);
                tempArray=query(condition);
                if(tempArray.size()>0){//已存在

                    result=2;//标记---自行改动部分

                    continue;//不执行任何操作
                }
            }

            //插入新纪录
            ContentValues cv=new ContentValues();
            cv.put("title",info.title);
            cv.put("release_time",info.release_time);
            cv.put("main_text",info.main_text);
            //执行插入记录动作,该语句返回插入记录的行号
            result=annDB.insert(TABLE_NAME,"",cv);
            //添加成功后返回行号,失败后返回-1
            if(result==-1){
                return result;
            }
        }
        return result;
    }

    //根据指定条件查询记录,并返回结果数据队列
    public ArrayList<AnnInfo> query(String condition){
        String sql=String.format("select title,release_time,main_text"+
                " from %s where %s;",TABLE_NAME,condition);
        ArrayList<AnnInfo> infoArray=new ArrayList<AnnInfo>();
        //执行记录查询动作,该语句返回结果集的游标
        Cursor cursor=annDB.rawQuery(sql,null);
        //循环取出游标指向的每条记录
        while(cursor.moveToNext()){
            AnnInfo info=new AnnInfo();
            info.title=cursor.getString(0);
            info.release_time=cursor.getString(1);
            info.main_text=cursor.getString(2);
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }

}
