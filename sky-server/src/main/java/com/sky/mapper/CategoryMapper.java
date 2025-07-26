package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> pageConditionalSelectAll(Category category);

    // 根据id修改分类信息（name and sort）
    @AutoFill(OperationType.UPDATE)
    @Update("update category set name = #{name}, sort = #{sort},update_time=#{updateTime},update_user=#{updateUser} where id = #{id}")
    void updateCategoryByID(Category category);

    // 根据id启用或禁用分类
    @Update("update category set status = #{status},update_user=#{updateUser},update_time=#{updateTime} where id = #{id}")
    void startOrStopCategoryByID(Category category);

    // 新增分类
    @AutoFill(OperationType.INSERT)
    void addCategory(Category category);

    // 根据id删除分类
    @Update("delete from category where id = #{id}")
    void deleteCategoryByID(Integer id);

    // 根据type查询分类，type为空时查询所有分类
    List<Category> searchAllByType(Integer type);

    // 根据id查询name：用于dish中显示每个菜品的所属的分类
    @Select("select name from category where id = #{id}")
    String getNameById(Integer id);
}
