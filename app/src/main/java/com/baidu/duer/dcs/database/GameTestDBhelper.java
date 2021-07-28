package com.baidu.duer.dcs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baidu.duer.dcs.bean.Game;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class GameTestDBhelper extends SQLiteOpenHelper {
    private static final String TAG = "GameTestDBhelper";
    private static final String DB_NAME="game.db";//数据库名称
    private static final int DB_VERSION =1;//数据库的版本号
    private static GameTestDBhelper mHelper=null;//数据库帮助器的实例
    private SQLiteDatabase mDB=null;//数据库的实例
    private static final String TABLE_NAME = "game_info";//表名

    private GameTestDBhelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    private GameTestDBhelper(Context context,int version){
        super(context,DB_NAME,null,version);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static GameTestDBhelper getInstance(Context context,int version){
        if(version>0&&mHelper==null){
            mHelper=new GameTestDBhelper(context,version);
        }else if(mHelper==null){
            mHelper=new GameTestDBhelper(context);
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
                +"q_id INTEGER NOT NULL,"
                +"img_src VARCHAR NOT NULL,"+"question VARCHAR NOT NULL,"
                +"answer VARCHAR NOT NULL,"+"tip VARCHAR NOT NULL,"
                +"pic INTEGER NOT NULL"+");";
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
    public long insert(Game info){
        ArrayList<Game> infoArray = new ArrayList<Game>();
        infoArray.add(info);
        return insert(infoArray);
    }
    //往该表添加多条记录
    public long insert(ArrayList<Game> infoArray){
        long result=-1;
        for (Game info:infoArray){
            Log.d(TAG,"q_id="+info.q_id+",img_src="+info.img_src+",question="+info.question+",answer="+info.answer+",tip="+info.tip+",pic="+info.pic);
            //如果存在相同标记q_id的记录,则更新记录,
            if(info.q_id>0){
                String condition =String.format("q_id='%d'",info.q_id);
                update(info,condition);
                result=info.q_id;
                continue;
            }
            //不存在唯一性重复的记录,则插入新记录
            ContentValues cv=new ContentValues();
            cv.put("q_id",info.q_id);
            cv.put("img_src",info.img_src);
            cv.put("question",info.question);
            cv.put("answer",info.answer);
            cv.put("tip",info.tip);
            cv.put("pic",info.pic);
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
    public int update(Game info,String condition){
        ContentValues cv=new ContentValues();
        cv.put("q_id",info.q_id);
        cv.put("img_src",info.img_src);
        cv.put("question",info.question);
        cv.put("answer",info.answer);
        cv.put("tip",info.tip);
        cv.put("pic",info.pic);
        //执行更新记录动作,该语句返回记录更新的数目
        return mDB.update(TABLE_NAME,cv,condition,null);
    }

    public int update(Game info){
        //执行更新记录动作,该语句返回记录更新的数目
        return update(info,"q_id="+info.q_id);
    }

    //根据指定条件查询记录,并返回结果数据队列
    public ArrayList<Game> query(String condition){
        String sql=String.format("select q_id,img_src,question,answer,tip,pic"+
                " from %s where %s;",TABLE_NAME,condition);
        Log.d(TAG,"query sql: "+sql);
        ArrayList<Game> infoArray=new ArrayList<Game>();
        //执行记录查询动作,该语句返回结果集的游标
        Cursor cursor=mDB.rawQuery(sql,null);
        //循环取出游标指向的每条记录
        while (cursor.moveToNext()){
            Game info=new Game();
            info.q_id=cursor.getInt(0);
            info.img_src=cursor.getString(1);
            info.question=cursor.getString(2);
            info.answer=cursor.getString(3);
            info.tip=cursor.getString(4);
            info.pic=cursor.getInt(5);
            infoArray.add(info);
        }
        cursor.close();//查询完毕,关闭游标
        return infoArray;
    }
    //根据q_id查询指定记录
    public Game queryByQId(int q_id){
        Game info=null;
        ArrayList<Game> infoArray = query(String.format("q_id='%d'",q_id));
        if(infoArray.size()>0){
            info=infoArray.get(0);
        }
        return info;
    }
}
