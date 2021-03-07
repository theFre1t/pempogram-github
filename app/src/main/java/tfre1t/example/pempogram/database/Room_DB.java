package tfre1t.example.pempogram.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;
import java.util.Objects;

import tfre1t.example.pempogram.SaveFile.Imager;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

public class Room_DB {
    private static final String TAG = "myLog";


    @Entity
    public static class Collection {

        @PrimaryKey(autoGenerate = true)
        public int id_collection;

        public String name_collection;

        public String author_collection;

        public String img_collection;
    }

    @Entity(foreignKeys = {@ForeignKey(entity = Collection.class, parentColumns = "id_collection", childColumns = "_id_collection", onDelete = CASCADE),
            @ForeignKey(entity = Audiofile.class, parentColumns = "id_audiofile", childColumns = "_id_audiofile", onDelete = CASCADE)},
            indices = @Index(value = {"_id_audiofile", "_id_collection"}, unique = true))
    public static class Collection_with_Audiofile {
        @PrimaryKey(autoGenerate = true)
        public int id;

        @ColumnInfo(index = true)
        public int _id_collection;

        @ColumnInfo(index = true)
        public int _id_audiofile;
    }

    @Entity(foreignKeys = @ForeignKey(entity = Collection.class, parentColumns = "id_collection", childColumns = "_id_collection", onDelete = SET_NULL),
            indices = @Index(value = {"id_audiofile","_id_collection"}, unique = true))
    public static class Audiofile {
        @PrimaryKey(autoGenerate = true)
        public int id_audiofile;

        public String name_audiofile;

        public String executor_audiofile;

        public String audiofile;

        @ColumnInfo(index = true)
        public int _id_collection;
    }

    @Entity
    public static class Categories {
        @PrimaryKey(autoGenerate = true)
        public int id_categories;

        public String name_categories;

        public String img_categories;
    }

    @Entity
    public static class Categories_with_Collection {
        @PrimaryKey(autoGenerate = true)
        public int id;
        public int _id_categories;
        public int _id_collection;
    }

    @Entity(foreignKeys = @ForeignKey(entity = Audiofile.class, parentColumns = "id_audiofile", childColumns = "_id_audiofile", onDelete = CASCADE))
    public static class FavoriteAudio {
        @PrimaryKey(autoGenerate = true)
        public int id_favau;

        @ColumnInfo(index = true)
        public int _id_audiofile;
    }

    @Entity(indices = @Index(value = {"revision_collection"}, unique = true))
    public static class Online_Collection {
        @PrimaryKey(autoGenerate = true)
        public int id_online_collection;

        public long revision_collection;

        public String name_collection;

        public String author_collection;

        public String public_url_collection;

        public String img_file_collection;

        public String img_preview_collection;
    }

    @Entity(foreignKeys = {@ForeignKey(entity = Online_Collection.class, parentColumns = "id_online_collection", childColumns = "_id_online_collection", onDelete = CASCADE),
                           @ForeignKey(entity = Collection.class, parentColumns = "id_collection", childColumns = "_id_collection", onDelete = CASCADE)},
            indices = @Index(value = {"_id_online_collection", "_id_collection"}, unique = true))
    public static class Online_Collection_with_Collection {
        @PrimaryKey(autoGenerate = true)
        public int id;

        @ColumnInfo(index = true)
        public int _id_online_collection;

        @ColumnInfo(index = true)
        public int _id_collection;
    }

    @Entity(foreignKeys = {@ForeignKey(entity = Online_Collection.class, parentColumns = "revision_collection", childColumns = "_revision_collection", onDelete = CASCADE)},
            indices = @Index(value = {"revision_audiofile", "_revision_collection"}, unique = true))
    public static class Online_Audiofile {
        @PrimaryKey(autoGenerate = true)
        public int id_online_audiofile;

        @ColumnInfo(index = true)
        public long revision_audiofile;

        public String name_audiofile;

        public String author_audiofile;

        public String audiofile;

        @ColumnInfo(index = true)
        public long _revision_collection;
    }

    @Entity(foreignKeys = {@ForeignKey(entity = Online_Audiofile.class, parentColumns = "id_online_audiofile", childColumns = "_id_online_audiofile", onDelete = CASCADE),
                           @ForeignKey(entity = Audiofile.class, parentColumns = "id_audiofile", childColumns = "_id_audiofile", onDelete = CASCADE)},
            indices = @Index(value = {"_id_online_audiofile", "_id_audiofile"}, unique = true))
    public static class Online_Audiofile_with_Audiofile {
        @PrimaryKey(autoGenerate = true)
        public int id;

        @ColumnInfo(index = true)
        public int _id_online_audiofile;

        @ColumnInfo(index = true)
        public int _id_audiofile;
    }

    /*@Entity
    public static class User {
        @PrimaryKey(autoGenerate = true)
        public int id_user;
        public String name_user;
        public String login_user;
        public String password_user;
        public String email_user;
    }

    @Entity(foreignKeys = @ForeignKey(entity = Audiofile.class, parentColumns = "id_audiofile", childColumns = "id_audiofile", onDelete = CASCADE))
    public static class MyAudiofile {
        @PrimaryKey(autoGenerate = true)
        public int id;
        public int id_audiofile;
        public String id_user;
    }

    @Entity
    public static class MyCollection {
        @PrimaryKey(autoGenerate = true)
        public int id;
        public int id_collection;
        public String id_user;
    }*/

    @Dao
    public interface CollectionDao{
        @Query("Select * From collection")
        LiveData<List<Collection>> getAll();

        @Query("Select * From collection Where name_collection LIKE :text OR author_collection LIKE :text")
        LiveData<List<Collection>> searchCollection(String text);

        @Query("Select * From collection Where id_collection = :id")
        LiveData<Collection> getById(int id);

        @Update
        void update(Collection collection);
    }

    @Dao
    public interface AudiofileDao{
        @Query("Select Au.*, Col.img_collection From audiofile as Au left join collection as Col on Au._id_collection = Col.id_collection")
        LiveData<List<Tables.AudiofileWithImg>> getAll();

        @Query("Select Au.*, Col.img_collection, Coll_w_Au._id_collection as id_collection_colli From Audiofile as Au left join Collection as Col" +
                " on Au._id_collection = Col.id_collection" +
                " left join Collection_with_Audiofile as Coll_w_Au" +
                " on Au.id_audiofile = Coll_w_Au._id_audiofile" +
                " Where Coll_w_Au._id_collection = :id")
        LiveData<List<Tables.AudiofileFull>> getAllByIdCollection(int id);

        @Query("Select Au.*, Coll_w_Au._id_collection as id_collection_colli From Audiofile as Au left join Collection_with_Audiofile as Coll_w_Au" +
                " on Au.id_audiofile = Coll_w_Au._id_audiofile" +
                " Where Au.id_audiofile = :id")
        LiveData<Tables.AudiofileWithColli> getAllById(int id);

        @Query("Select Au.*, Col.img_collection From audiofile as Au left join collection as Col on Au._id_collection = Col.id_collection" +
                " Where Au.id_audiofile = :id")
        LiveData<Tables.AudiofileWithImg> getById(int id);

        @Query("Select Au.*, Col.img_collection From audiofile as Au left join collection as Col on Au._id_collection = Col.id_collection" +
                " Where Au.id_audiofile = :id")
        Tables.AudiofileWithImg getNonLiveById(int id);

        @Query("Update Audiofile Set name_audiofile = :name, executor_audiofile = :executor Where id_audiofile = :id")
        void update(int id, String name, String executor);
    }

    @Dao
    public interface CategoriesDao{
        @Query("Select * From Categories")
        LiveData<List<Categories>> getAll();

        @Query("Select * From Categories Where id_categories = :id")
        LiveData<Categories> getById(int id);

        @Insert
        void insert(Categories categories);

        @Update
        void update(Categories categories);

        @Delete
        void delete(Categories categories);
    }

    @Dao
    public interface FavoriteAudioDao{
        @Query("Select Au.*, Col.img_collection From audiofile as Au left join collection as Col" +
                " on Au._id_collection = Col.id_collection" +
                " left join FavoriteAudio as Fav" +
                " on Au.id_audiofile = Fav._id_audiofile" +
                " Where Fav._id_audiofile is null")
        LiveData<List<Tables.AudiofileWithImg>> getAllNonFavAu();

        @Query("Select Au.*, Col.img_collection From audiofile as Au left join collection as Col" +
                " on Au._id_collection = Col.id_collection" +
                " left join FavoriteAudio as Fav" +
                " on Au.id_audiofile = Fav._id_audiofile" +
                " Where Fav._id_audiofile is null and" +
                " (Au.name_audiofile LIKE :text OR Au.executor_audiofile LIKE :text)")
        LiveData<List<Tables.AudiofileWithImg>> searchAllNonFavAu(String text);

        @Query("Select Au.*, Col.img_collection From audiofile as Au inner join collection as Col" +
                " on Au._id_collection = Col.id_collection" +
                " inner join FavoriteAudio as Fav" +
                " on Au.id_audiofile = Fav._id_audiofile")
        LiveData<List<Tables.AudiofileWithImg>> getAll();

        @Query("Select * From FavoriteAudio Where id_favau = :id")
        LiveData<FavoriteAudio> getById(int id);

        @Insert
        void insert(FavoriteAudio favoriteAudio);

        @Update
        void update(FavoriteAudio favoriteAudio);

        @Query("Delete from FavoriteAudio Where _id_audiofile = :id_audiofile")
        void delete(int id_audiofile);
    }

    @Dao
    public interface Collection_with_AudiofileDao{
        @Query("Select * From Collection_with_Audiofile")
        LiveData<List<Collection_with_Audiofile>> getAll();

        @Query("Select * From Collection_with_Audiofile Where _id_collection = :id")
        LiveData<Collection_with_Audiofile> getById(int id);

        @Insert
        void insert(Collection_with_Audiofile collection_with_audiofile);

        @Update
        void update(Collection_with_Audiofile collection_with_audiofile);

        @Query("Delete from Collection_with_Audiofile Where _id_collection = :idColl and _id_audiofile = :idAud")
        void delete(int idColl, int idAud);
    }

    @Dao
    public interface Categories_with_CollectionDao{
        @Query("Select * From Categories_with_Collection")
        LiveData<List<Categories_with_Collection>> getAll();

        @Query("Select * From Categories_with_Collection Where id = :id")
        LiveData<Categories_with_Collection> getById(int id);

        @Insert
        void insert(Categories_with_Collection categoriesLeftIn);

        @Update
        void update(Categories_with_Collection categoriesLeftIn);

        @Delete
        void delete(Categories_with_Collection categoriesLeftIn);
    }

    @Dao
    public interface Online_CollectionDao{
        @Query("Select * From Online_Collection")
        LiveData<List<Online_Collection>> getAll();

        @Query("Select * From Online_Collection Where id_online_collection = :id")
        LiveData<Online_Collection> getById(int id);

        @Query("Select * From Online_Collection Where name_collection LIKE :text OR author_collection LIKE :text")
        LiveData<List<Online_Collection>> searchOnlineCollection(String text);

        @Insert
        void insert(Online_Collection online_collection);

        @Update
        void update(Online_Collection online_collection);

        @Delete
        void delete(Online_Collection online_collection);
    }

    @Dao
    public interface Online_AudiofileDao{
        @Query("Select * From Online_Audiofile")
        LiveData<List<Online_Audiofile>> getAll();

        @Query("Select * From Online_Audiofile Where id_online_audiofile = :id")
        LiveData<Online_Audiofile> getById(int id);

        @Query("Select Au.* From Online_Audiofile as Au left join Online_Collection as Col" +
                " on Au._revision_collection = Col.revision_collection" +
                " Where Col.id_online_collection = :id")
        LiveData<List<Room_DB.Online_Audiofile>> getAllByIdCollection(int id);

        @Query("Select * From Online_Audiofile Where id_online_audiofile = :id")
        Room_DB.Online_Audiofile getNonLiveById(int id);

        @Insert
        void insert(Online_Audiofile online_audiofile);

        @Update
        void update(Online_Audiofile online_audiofile);

        @Delete
        void delete(Online_Audiofile online_audiofile);
    }

    @Dao
    public static abstract class CollectionDao_abstract{
        @Insert
        protected abstract void insertCollection(Collection collection);

        @Transaction
        public void insert(Collection collection){
            if(collection.img_collection == null){
                collection.img_collection = "default.png";
            }
            insertCollection(collection);
        }

        @Query("Select audiofile From audiofile Where _id_collection = :id")
        protected abstract List<String> getAudiofiles(int id);

        @Query("Delete from Audiofile Where _id_collection = :id")
        protected abstract void deleAllAudiofilesByIdCollection(int id);

        @Delete
        protected abstract void deleteCollection(Collection collection);

        @Transaction
        public void delete(Context ctx, Collection collection, boolean full){
            new Imager().deleteImage(ctx, collection.img_collection);
            if (full){
                List<String> audiofiles = getAudiofiles(collection.id_collection);
                for (String audiofile: audiofiles) {
                    ctx.deleteFile(audiofile);
                }
                deleAllAudiofilesByIdCollection(collection.id_collection);
            }
            deleteCollection(collection);
        }
    }

    @Dao
    public abstract static class AudiofileDao_abstract{
        @Insert
        protected abstract void insertAudiofile(Audiofile audiofile);

        @Query("Select last_insert_rowid()")
        protected abstract int getInsertAudiofile();

        @Insert
        protected abstract void insertCollection_left_in(Collection_with_Audiofile collection_with_audiofile);

        @Transaction
        public void insert(Audiofile aud){
            Collection_with_Audiofile collection_with_audiofile = new Collection_with_Audiofile();
            insertAudiofile(aud);
            collection_with_audiofile._id_audiofile = getInsertAudiofile();
            collection_with_audiofile._id_collection = aud._id_collection;
            insertCollection_left_in(collection_with_audiofile);
        }

        @Delete
        protected abstract void deleteAudiofile(Audiofile audiofile);

        @Transaction
        public void delete(Context ctx, Audiofile audiofile){
            ctx.deleteFile(audiofile.name_audiofile);
            deleteAudiofile(audiofile);
        }
    }

    @Dao
    public static abstract class Online_CollectionDao_abstract{
        @Query("Select * From Online_Collection Where revision_collection = :revision")
        protected abstract Online_Collection getByRevision(long revision);

        @Insert
        protected abstract void insertCollection(Online_Collection online_collection);

        @Update
        protected abstract void updateCollection(Online_Collection online_collection);

        @Transaction
        public void insUpd(long revision, String name_coll, String author_coll){
            Online_Collection onlineCollection = getByRevision(revision);
            if(Objects.equals(onlineCollection, null)){
                onlineCollection = new Online_Collection();
                onlineCollection.revision_collection = revision;
                onlineCollection.name_collection = name_coll;
                onlineCollection.author_collection = author_coll;
                insertCollection(onlineCollection);
            }
            else if(!onlineCollection.name_collection.equals(name_coll) ||
                    !onlineCollection.author_collection.equals(author_coll)){
                onlineCollection.name_collection = name_coll;
                onlineCollection.author_collection = author_coll;
                updateCollection(onlineCollection);
            }
        }

        @Transaction
        public void updateImage(long revision, String img_file, String img_preview){
            Online_Collection onlineCollection = getByRevision(revision);
            if(!onlineCollection.equals(null)){
                onlineCollection.img_file_collection = img_file;
                onlineCollection.img_preview_collection = img_preview;
                updateCollection(onlineCollection);
            }
        }

        /*@Query("Select audiofile From audiofile Where _id_collection = :id")
        protected abstract List<String> getAudiofiles(int id);

        @Query("Delete from Audiofile Where _id_collection = :id")
        protected abstract void deleAllAudiofilesByIdCollection(int id);

        @Delete
        protected abstract void deleteCollection(Collection collection);

        @Transaction
        public void delete(Context ctx, Collection collection, boolean full){
            new Imager().deleteImage(ctx, collection.img_collection);
            if (full){
                List<String> audiofiles = getAudiofiles(collection.id_collection);
                for (String audiofile: audiofiles) {
                    ctx.deleteFile(audiofile);
                }
                deleAllAudiofilesByIdCollection(collection.id_collection);
            }
            deleteCollection(collection);
        }*/
    }

    @Dao
    public abstract static class Online_AudiofileDao_abstract{

        @Query("Select * From Online_Audiofile Where revision_audiofile = :revision")
        protected abstract Online_Audiofile getByRevision(long revision);

        @Insert
        protected abstract void insertAudio(Online_Audiofile online_audiofile);

        @Update
        protected abstract void updateAudio(Online_Audiofile online_audiofile);

        @Transaction
        public void insUpd(long rev_id, String name, String author, String file_url, long coll_rev) {
            //Получаем запись по revision
            Online_Audiofile online_audiofile = getByRevision(rev_id);
            //Проверяем на наличие записи
            if (Objects.equals(online_audiofile, null)) {
                //Если записи нет, то добавляем
                online_audiofile = new Online_Audiofile();
                online_audiofile.revision_audiofile = rev_id;
                online_audiofile.name_audiofile = name;
                online_audiofile.author_audiofile = author;
                online_audiofile.audiofile = file_url;
                online_audiofile._revision_collection = coll_rev;
                insertAudio(online_audiofile);
            } else if (!online_audiofile.name_audiofile.equals(name) || !online_audiofile.author_audiofile.equals(author) || !online_audiofile.audiofile.equals(file_url)) {
                //Если записи есть, то обновляем
                online_audiofile.name_audiofile = name;
                online_audiofile.author_audiofile = author;
                online_audiofile.audiofile = file_url;
                updateAudio(online_audiofile);
            }
        }
        /*@Insert

        protected abstract void insertAudiofile(Audiofile audiofile);

        @Query("Select last_insert_rowid()")
        protected abstract int getInsertAudiofile();

        @Insert
        protected abstract void insertCollection_left_in(Collection_left_in collectionLeftIn);

        @Transaction
        public void insert(Audiofile aud){
            Collection_left_in collectionLeftIn = new Collection_left_in();
            insertAudiofile(aud);
            collectionLeftIn._id_audiofile = getInsertAudiofile();
            collectionLeftIn._id_collection = aud._id_collection;
            insertCollection_left_in(collectionLeftIn);
        }

        @Delete
        protected abstract void deleteAudiofile(Audiofile audiofile);

        @Transaction
        public void delete(Context ctx, Audiofile audiofile){
            ctx.deleteFile(audiofile.name_audiofile);
            deleteAudiofile(audiofile);
        }*/
    }

    @Database(entities = {Collection.class, Audiofile.class, Categories.class, FavoriteAudio.class, Collection_with_Audiofile.class, Categories_with_Collection.class,
                          Online_Collection.class, Online_Collection_with_Collection.class, Online_Audiofile.class, Online_Audiofile_with_Audiofile.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase{
        public abstract CollectionDao collectionDao();
        public abstract CollectionDao_abstract collectionDaoAbstr();
        public abstract AudiofileDao audiofileDao();
        public abstract AudiofileDao_abstract audiofileDaoAbstr();
        public abstract CategoriesDao categoriesDao();
        public abstract FavoriteAudioDao favoriteAudioDao();
        public abstract Collection_with_AudiofileDao collectionWithAudiofileDao();
        public abstract Categories_with_CollectionDao categoriesWithCollectionDao();
        public abstract Online_CollectionDao onlineCollectionDao();
        public abstract Online_AudiofileDao onlineAudiofileDao();
        public abstract Online_CollectionDao_abstract onlineCollectionDaoAbstr();
        public abstract Online_AudiofileDao_abstract onlineAudiofileDaoAbstr();
    }
}