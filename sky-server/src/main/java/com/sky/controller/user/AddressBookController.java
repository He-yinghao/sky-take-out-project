package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {
    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    // 新增地址
    @PostMapping
    public Result<Object> add(@RequestBody AddressBook addressBook) {
        addressBookService.addAddress(addressBook);
        return Result.success();
    }

    // 查询当前登录用户的所有地址
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    // 查询默认地址
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.getFirst());
        }

        return Result.error("没有查询到默认地址");
    }

    // 根据id修改地址
    @PutMapping
    public Result<Object> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateAddress(addressBook);
        return Result.success();
    }

    // 根据id删除地址
    @DeleteMapping
    public Result<Object> deleteById(@RequestParam Integer id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    // 根据id查询地址
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Integer id) {
        return Result.success(addressBookService.getAddressById(id));
    }

    // 设置默认地址
    @PutMapping("/default")
    public Result<Object> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefaultAddress(addressBook);
        return Result.success();
    }
}
