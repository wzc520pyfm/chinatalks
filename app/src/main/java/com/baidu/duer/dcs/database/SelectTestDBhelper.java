package com.baidu.duer.dcs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baidu.duer.dcs.bean.Select;

import java.util.ArrayList;
/****************************************************************************************************
 * 类:                Select数据库帮助类
 * 用途:              实现数据存储
 *
 * ==================================================================================================*/
public class SelectTestDBhelper extends SQLiteOpenHelper {
    private static final String TAG = "SelectTestDBHelper";
    private static final String DB_NAME="select.db";//数据库名称
    private static final int DB_VERSION =1;//数据库的版本号
    private static SelectTestDBhelper mHelper=null;//数据库帮助器的实例
    private SQLiteDatabase mDB=null;//数据库的实例
    private static final String TABLE_NAME = "select_info";//表名

    private SelectTestDBhelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    private SelectTestDBhelper(Context context, int version){
        super(context,DB_NAME,null,version);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static SelectTestDBhelper getInstance(Context context, int version){
        if(version>0&&mHelper==null){
            mHelper=new SelectTestDBhelper(context,version);
        }else if(mHelper==null){
            mHelper=new SelectTestDBhelper(context);
        }
        return mHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(mDB==null || !mDB.isOpen()){
            mDB=mHelper.getReadableDatabase();
        }
        return mDB;
    }
    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(mDB==null||!mDB.isOpen()){
            mDB=mHelper.getWritableDatabase();
        }
        return mDB;
    }
    //关闭数据连接
    public void closeLink(){
        if(mDB!=null&&mDB.isOpen()){
            mDB.close();
            mDB=null;
        }
    }

    //创建数据库,执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG,"onCreate");
        String drop_sql="DROP TABLE IF EXISTS " + TABLE_NAME +";";
        Log.d(TAG,"onCreate");
        db.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+"("
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +"Qno INTEGER NOT NULL,"
                +"question VARCHAR NOT NULL,"+"answer VARCHAR NOT NULL,"
                +"item1 VARCHAR NOT NULL,"+"item2 VARCHAR NOT NULL,"
                +"item3 VARCHAR NOT NULL,"+"item4 VARCHAR NOT NULL,"
                +"desc VARCHAR NOT NULL,"+"tip VARCHAR NOT NULL,"
                +"key_word VARCHAR NOT NULL"+");";
        Log.d(TAG,"create_sql:"+create_sql);
        db.execSQL(create_sql);
    }
    //修改数据库,执行表结构变更语句
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
    //根据指定条件删除表记录
    public int delete(String condition){
        //执行删除记录动作,该语句返回删除记录的数目
        return mDB.delete(TABLE_NAME,condition,null);
    }
    //删除该表的所有记录
    public int deleteAll(){
        //执行删除记录动作,该语句返回删除记录的数目
        return mDB.delete(TABLE_NAME,"1=1",null);
    }
    //往表中添加一条记录
    public long insert(Select info){
        ArrayList<Select> infoArray = new ArrayList<Select>();
        infoArray.add(info);
        return insert(infoArray);
    }
    //往该表添加多条记录
    public long insert(ArrayList<Select> infoArray){
        long result=-1;
        for (Select info:infoArray){
            Log.d(TAG,"Qno="+info.Qno+",question="+info.question+",answer="+info.answer+",item1="+info.item1+",item2="+info.item2+",item3="+info.item3+",item4="+info.item4+",desc="+info.desc+",tip="+info.tip+",key_word="+info.key_word);
            //如果存在相同标记wno的记录,则更新记录,
            if(info.Qno>0){
                String condition =String.format("Qno='%d'",info.Qno);
                update(info,condition);
                result=info.Qno;
                continue;
            }
            //不存在唯一性重复的记录,则插入新记录
            ContentValues cv=new ContentValues();
            cv.put("Qno",info.Qno);
            cv.put("question",info.question);
            cv.put("answer",info.answer);
            cv.put("item1",info.item1);
            cv.put("item2",info.item2);
            cv.put("item3",info.item3);
            cv.put("item4",info.item4);
            cv.put("desc",info.desc);
            cv.put("tip",info.tip);
            cv.put("key_word",info.key_word);
            //执行插入记录动作,该语句返回插入记录的行号
            result=mDB.insert(TABLE_NAME,"",cv);
            //添加成功后返回行号,失败后返回-1
            if(result==-1){
                return result;
            }
        }
        return result;
    }

    //根据条件更新指定的表记录
    public int update(Select info,String condition){
        ContentValues cv=new ContentValues();
        cv.put("Qno",info.Qno);
        cv.put("question",info.question);
        cv.put("answer",info.answer);
        cv.put("item1",info.item1);
        cv.put("item2",info.item2);
        cv.put("item3",info.item3);
        cv.put("item4",info.item4);
        cv.put("desc",info.desc);
        cv.put("tip",info.tip);
        cv.put("key_word",info.key_word);
        //执行更新记录动作,该语句返回记录更新的数目
        return mDB.update(TABLE_NAME,cv,condition,null);
    }

    public int update(Select info){
        //执行更新记录动作,该语句返回记录更新的数目
        return update(info,"Qno="+info.Qno);
    }

    //根据指定条件查询记录,并返回结果数据队列
    public ArrayList<Select> query(String condition){
        String sql=String.format("select Qno,question,answer,item1,item2,item3,item4,desc,tip,key_word"+
                " from %s where %s;",TABLE_NAME,condition);
        Log.d(TAG,"query sql: "+sql);
        ArrayList<Select> infoArray=new ArrayList<Select>();
        //执行记录查询动作,该语句返回结果集的游标
        Cursor cursor=mDB.rawQuery(sql,null);
        //循环取出游标指向的每条记录
        while (cursor.moveToNext()){
            Select info=new Select();
            info.Qno=cursor.getInt(0);
            info.question=cursor.getString(1);
            info.answer=cursor.getString(2);
            info.item1=cursor.getString(3);
            info.item2=cursor.getString(4);
            info.item3=cursor.getString(5);
            info.item4=cursor.getString(6);
            info.desc=cursor.getString(7);
            info.tip=cursor.getString(8);
            info.key_word=cursor.getString(9);


            infoArray.add(info);
        }
        cursor.close();//查询完毕,关闭游标
        return infoArray;
    }
    //根据q_id查询指定记录
    public Select queryByQId(int Qno){
        Select info=null;
        ArrayList<Select> infoArray = query(String.format("Qno='%d'",Qno));
        if(infoArray.size()>0){
            info=infoArray.get(0);
        }
        return info;
    }
}
