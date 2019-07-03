package cn.happyjava.springbootspringsecurityjwtmybatisplus.mapper;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.entity.AdminEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * 使用Component注解主要是为了不然IDEA报错，其实不用也是可以的
 *
 * @author happy
 */
@Component
@Mapper
public interface AdminMapper extends BaseMapper<AdminEntity> {


}

