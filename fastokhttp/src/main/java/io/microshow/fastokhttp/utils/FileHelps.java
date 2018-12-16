package io.microshow.fastokhttp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 文件辅助类
 * @author john
 *
 */
public class FileHelps {

	/**
	 * SD卡的root path
	 */
	public static final String sdrootpath = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private static final String sdpath = "/com.superman.droidokhttp/";
	
	/**
	 * 根据url 文件地址，生成一个sd对应的目录文件
	 * @param urlPath
	 * @return
	 */
	public static String getModelFileSDPath (String urlPath) {
		//例如urlPath = www.xxx.com/data/xxx.zip，则modelSDPath = data/xxx.zip
		String modelSDPath = urlPath.substring(urlPath.indexOf("com")+4);
		String tempPath = FileHelps.sdrootpath + sdpath + modelSDPath;
		return tempPath;
	}
	
	/**
	 * 根据url 文件地址，获取一个sd对应的解压目录
	 * @param urlPath
	 * @return
	 */
	public static String getModelFileUnzipSDPath (String urlPath) {
		//例如urlPath = www.xxx.com/data/xxx.zip，则modelSDPath = data/xxx
		String modelSDPath = urlPath.substring(urlPath.indexOf("com")+4, urlPath.lastIndexOf("."));
		String tempPath = FileHelps.sdrootpath + sdpath + modelSDPath + File.separator;
		return tempPath;
	}
	
	/**
	 * 创建模型文件  ：根据url 文件地址，生成一个sd对应的目录文件
	 * @param urlPath
	 * @return
	 */
	public static void createModelFile (String sdPath) {
		createFile(sdPath);
	}
	
	/**
	 * 创建文件
	 * @param destFileName
	 * @return
	 */
	public static boolean createFile(String destFileName) {  
        File file = new File(destFileName);  
        if(file.exists()) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
            return false;  
        }  
        if (destFileName.endsWith(File.separator)) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
            return false;  
        }  
        //判断目标文件所在的目录是否存在  
        if(!file.getParentFile().exists()) {  
            //如果目标文件所在的目录不存在，则创建父目录  
            System.out.println("目标文件所在目录不存在，准备创建它！");  
            if(!file.getParentFile().mkdirs()) {  
                System.out.println("创建目标文件所在目录失败！");  
                return false;  
            }  
        }  
        //创建目标文件  
        try {  
            if (file.createNewFile()) {  
                System.out.println("创建单个文件" + destFileName + "成功！");  
                return true;  
            } else {  
                System.out.println("创建单个文件" + destFileName + "失败！");  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
            return false;  
        }  
    }
	
	/**
	 * 创建.nomedia 文件，防止图片被系统图库检测到
	 * @param filePath
	 */
	public static void createNomediaFile (String filePath) {
		File nomedia = new File(filePath + ".nomedia" );  
		if (! nomedia.exists())  
			try {  
				nomedia.createNewFile();  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
	}
	
	public static String saveFileToSD(byte[] data, String mime) {
		try {
			File openFile = new File(sdrootpath, sdpath);
			if(!openFile.exists()) {
				openFile.mkdirs();
			}
			
			File temFile = new File(openFile, mime);
			if(!temFile.exists()){
				temFile.mkdirs();
			}
			
			File mimeFile = new File(temFile, getRandomString(16));
			if(!mimeFile.exists()){
				mimeFile.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(mimeFile);

			fos.write(data);
			fos.flush();
			fos.close();
			return mimeFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取一个随机字符串
	 * @param length 表示生成字符串的长度
	 * @return
	 */
	public static String getRandomString(int length) {
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }
	
	/**
	 * 删除所有文件
	 * @param filepath
	 * @throws IOException
	 */
	public static void deleteAllFile(String filepath) throws IOException{
		File f = new File(filepath);//定义文件路径       
		if(f.exists() && f.isDirectory()){//判断是文件还是目录
			if(f.listFiles().length==0){//若目录下没有文件则直接删除
				f.delete();
			}else{//若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for(int j = 0; j < i; j ++){
					if(delFile[j].isDirectory()){
						deleteAllFile(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径
					}
					delFile[j].delete();//删除文件
				}
			}
		}    
	}
	
	public static String getPath(Context context, Uri uri) {
		 
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
 
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
 
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
 
        return null;
    }
	
	public static boolean makeFolders(File filePath) {
        return makeDirs(filePath.getAbsolutePath());
    }
	
	/**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link File# makeFolder}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    
    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

}
