package com.games.common.utils;

import cn.hutool.core.util.StrUtil;
import com.games.common.utils.spring.SpringUtils;
import org.springframework.context.MessageSource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取i18n资源文件
 * 
 * @author lor
 */
@Slf4j
public class MessageUtils
{

    public static boolean isZh(){
        return StrUtil.equalsIgnoreCase(getLan(), "zh_cn");
    }

    public static String getLan(){
        String language = null;
        try{
            HttpServletRequest request = ServletUtils.getRequest();
            if(request != null){
                language = request.getHeader("lang");
            }
            if(StrUtil.isBlank(language))
            {
                language = ServletUtils.getParameter("lang");
            }
        } catch (Exception e) {
            log.debug("无法从当前请求读取语言设置", e);
        }
        if(StrUtil.isBlank(language)){
            language = "en_US";
        }
        return language;
    }

    public static String dictI18n(String code){
//        try{
//            return message("DICT_" + code, "");
//        }catch (Exception e){
            return code;
//        }
    }

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args)
    {
        try {
            String language = getLan();
            //如果没有就使用默认的（根据主机的语言环境生成一个 Locale
            Locale locale = Locale.getDefault();
            //如果请求的链接中携带了 国际化的参数
            if (!StrUtil.isEmpty(language)) {
                //zh_CN
                String[] s = language.split("_");
                //国家，地区
                locale = new Locale(s[0], s[1]);
            }
            MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
            return messageSource.getMessage(code, args, locale);
//        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        }catch (Exception e){
            return code;
        }

    }
}
