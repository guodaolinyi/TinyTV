package com.tinywo.tinytv.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tinywo.tinytv.cache.Cache;
import com.tinywo.tinytv.cache.CacheDao;
import com.tinywo.tinytv.cache.VodCollect;
import com.tinywo.tinytv.cache.VodCollectDao;
import com.tinywo.tinytv.cache.VodRecord;
import com.tinywo.tinytv.cache.VodRecordDao;


/**
 * 类描述:
 *
 * @author pj567
 * @since 2020/5/15
 */
@Database(entities = {Cache.class, VodRecord.class, VodCollect.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CacheDao getCacheDao();

    public abstract VodRecordDao getVodRecordDao();

    public abstract VodCollectDao getVodCollectDao();
}
