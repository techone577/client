package com.eureka.client.model.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author techoneduan
 * @date 2018/12/20
 */
public class BspSubscribeService {

    public static List<String> subscribeList = new ArrayList<String>(){
        {
            add("BLOG.TestController.test");
        }
    };
}
