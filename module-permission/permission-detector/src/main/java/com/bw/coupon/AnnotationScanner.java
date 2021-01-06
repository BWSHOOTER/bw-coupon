package com.bw.coupon;

import com.bw.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 接口权限信息扫描器
 * @Author: BaoWei
 * @Date: 2021/01/06 16:23
 */
@Slf4j
public class AnnotationScanner {
    private static final String TAR_PACKAGE = "com.bw.coupon.controller";
    private String urlPrefix;

    /** 构造单个接口的权限信息 */
    private PermissionInfo buildPermissionInfo(String javaMethod,
                                               String url,
                                               String httpMethod,
                                               boolean isReadOnly,
                                               String description,
                                               String extra){
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.setUrl(url);
        permissionInfo.setHttpMethod(httpMethod);
        permissionInfo.setReadOnly(isReadOnly);
        permissionInfo.setDescription(description);
        permissionInfo.setDescription(StringUtils.isEmpty(description)? javaMethod: description);
        permissionInfo.setExtra(extra);
        return permissionInfo;
    }

    /** 判断类是否在目标扫描目录中 */
    private boolean isTarPackage(String className){
        return className.startsWith(TAR_PACKAGE);
    }

    /** 保证 path 以 / 开头，不以 / 结尾 */
    private String trimPath(String path){
        if (StringUtils.isEmpty(path))
            return "";
        if (!path.startsWith("/"))
            path = "/" + path;
        if (path.endsWith("/"))
            path = path.substring(0, path.length()-1);
        return path;
    }
}
