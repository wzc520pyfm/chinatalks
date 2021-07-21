package com.baidu.duer.dcs.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baidu.duer.dcs.bean.TestCenter;

import java.util.ArrayList;

//@SuppressLint("DefaultLocale")
public class TestCenterDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "TestCenterDBHelper";
    private static final String DB_NAME = "test.db";//数据库名称
    private static final int DB_VERSION = 1;//数据库的版本号
    private static TestCenterDBHelper mHelper = null;//数据库帮助器的实例
    private SQLiteDatabase mDB=null;//数据库的实例
    private static final String TABLE_NAME = "test_info";//表名

    private TestCenterDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    private TestCenterDBHelper(Context context,int version){
        super(context,DB_NAME,null,version);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static TestCenterDBHelper getInstance(Context context,int version){
        if(version > 0 && mHelper == null){
            mHelper = new TestCenterDBHelper(context,version);
        }else if(mHelper == null){
            mHelper = new TestCenterDBHelper(context);
        }
        return mHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(mDB == null || !mDB.isOpen()){
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }
    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(mDB == null ||!mDB.isOpen()){
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }
    //关闭数据库连接
    public void closeLink(){
        if(mDB != null && mDB.isOpen()){
            mDB.close();
            mDB = null;
        }
    }

    //创建数据库,执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG,"onCrete");
        String drop_sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        Log.d(TAG,"drop_sql:" + drop_sql);
        db.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +"test_id LONG NOT NULL,"
                + "title VARCHAR NOT NULL,"+"desc VARCHAR NOT NULL,"
                +"finshed_num INTEGER NOT NULL,"+"score INTEGER NOT NULL,"
                +"test_time VARCHAR NOT NULL"
                +");";
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
    public long insert(TestCenter info){
        ArrayList<TestCenter> infoArray = new ArrayList<TestCenter>();
        infoArray.add(info);
        return insert(infoArray);
    }
    //往该表添加多条记录
    public long insert(ArrayList<TestCenter> infoArray){
        long result=-1;
        for(TestCenter info : infoArray){
            Log.d(TAG,"test_id="+info.test_id+"title="+info.title+",desc="+info.desc+",finsged_num="+info.finshed_num+",score="+info.score+",test_time="+info.test_time);
            //如果存在相同标记test_id的记录,则更新记录---类似行号,如果是已有的记录,应附带此id,如果是插入新纪录,此id应由表自动生成,
            if(info.rowid>0){
                String condition = String.format("rowid='%d'",info.rowid);
                update(info,condition);
                result=info.rowid;
                continue;
            }
            //不存在唯一性重复的记录,则插入新记录
            ContentValues cv=new ContentValues();
            cv.put("test_id",info.test_id);
            cv.put("title",info.title);
            cv.put("desc",info.desc);
            cv.put("finshed_num",info.finshed_num);
            cv.put("score",info.score);
            cv.put("test_time",info.test_time);
            //执行插入记录动作,该语句返回插入记录的行号
            result=mDB.insert(TABLE_NAME,"",cv);
            //添加成功后返回行号,失败后返回-1
            if(result == -1){
                return result;
            }
        }
        return result;
    }
    //根据条件更新指定的表记录
    public int update(TestCenter info,String condition){
        ContentValues cv=new ContentValues();
        cv.put("test_id",info.test_id);
        cv.put("title",info.title);
        cv.put("desc",info.desc);
        cv.put("finshed_num",info.finshed_num);
        cv.put("score",info.score);
        cv.put("test_time",info.test_time);
        //执行更新记录动作,该语句返回记录更新的数目
        return mDB.update(TABLE_NAME,cv,condition,null);
    }

    public int update(TestCenter info){
        //执行更新记录动作,该语句返回记录更新的数目
        return update(info,"rowid="+info.rowid);
    }

    //根据指定条件查询记录,并返回结果数据队列
    public ArrayList<TestCenter> query(String condition){
        String sql=String.format("select rowid,_id,test_id,title,desc,finshed_num,score,test_time"+
                " from %s where %s;", TABLE_NAME,condition);
        Log.d(TAG,"query sql: "+sql);
        ArrayList<TestCenter> infoArray = new ArrayList<TestCenter>();
        //执行记录查询动作,该语句返回结果集的游标
        Cursor cursor = mDB.rawQuery(sql,null);
        //循环取出游标指向的每条记录
        while (cursor.moveToNext()){
            TestCenter info = new TestCenter();
            info.rowid=cursor.getLong(1);
            info.test_id=cursor.getLong(2);
            info.title=cursor.getString(3);
            info.desc=cursor.getString(4);
            info.finshed_num=cursor.getInt(5);
            info.score=cursor.getInt(6);
            info.test_time=cursor.getString(7);
            Log.d(TAG,info.rowid+" "+info.test_id+" "+info.title+" "+info.desc+" "+info.finshed_num+" "+info.score+" "+info.test_time);
            infoArray.add(info);
        }
        cursor.close();//查询完毕,关闭游标
        return infoArray;
    }
    //根据行号查询指定记录
    public TestCenter queryById(long rowid){
        TestCenter info=null;
        ArrayList<TestCenter> infoArray = query(String.format("rowid='%d'",rowid));
        if(infoArray.size()>0){
            info=infoArray.get(0);
        }
        return info;
    }
    //根据试卷编号查询指定记录
    public TestCenter queryByTestId(long test_id){
        TestCenter info=null;
        ArrayList<TestCenter> infoArray=query(String.format("test_id='%d'",test_id));
        if(infoArray.size()>0){
            info=infoArray.get(0);
        }
        return info;
    }

}
