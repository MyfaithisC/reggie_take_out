package reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import reggie.pojo.AddressBook;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
