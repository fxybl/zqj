package com.zqj.baidu.ai;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * @author zqj
 * @create 2019-11-14 14:24
 */
public class Test {

    //设置APPID/AK/SK
    public static final String APP_ID = "17770105";
    public static final String API_KEY = "gRkOhvBfxQ4ZgGxGBqejvGqN";
    public static final String SECRET_KEY = "r9LGjRFqBmwjsHcPpwmyb4PGFSjYrHrz";

    public static void main(String[] args) throws  Exception {
        // 初始化一个AipBodyAnalysis
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        File files = new File("C:\\Users\\Administrator\\Desktop\\to_daxia");
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        for(File file : files.listFiles()){
            // 调用接口
            String path = file.getAbsolutePath();
            JSONObject res = client.bodyAnalysis(path, new HashMap<String, String>());
            try {
                System.out.println(file.getName()+"图片人数为:"+res.get("person_num"));
                Thread.sleep(3000);
            }catch (Exception e){
                System.out.println(file.getName()+"识别失败，结果为:"+res.toString());
            }

        }






    }
}
