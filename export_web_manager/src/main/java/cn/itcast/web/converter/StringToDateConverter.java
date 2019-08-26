package cn.itcast.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

@Component
public class StringToDateConverter implements Converter<String,Date> {

    @Override
    public Date convert(String source) {

        try {
            //判断是否为空
            if (StringUtils.isEmpty(source)){
                return null;
            }

            //转换
            return new SimpleDateFormat("yyyy-MM-dd").parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
