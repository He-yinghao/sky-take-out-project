package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    private final AddressBookMapper addressBookMapper;

    public AddressBookServiceImpl(AddressBookMapper addressBookMapper) {
        this.addressBookMapper = addressBookMapper;
    }

    @Override
    public void addAddress(AddressBook addressBook) {
        // 获取当前发出请求用户id
        Integer userID = BaseContext.getCurrentId();
        addressBook.setUserId(userID);
        // 新增地址的默认地址为0，即均不是默认地址
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        // 获取当前发出请求用户id
        return addressBookMapper.list(addressBook);
    }



    @Override
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void deleteById(Integer id) {
        AddressBook addressBook = AddressBook.builder()
                .id(id)
                .build();
        addressBookMapper.deleteById(addressBook);
    }

    @Override
    public AddressBook getAddressById(Integer id) {
        return addressBookMapper.getAddressById(id);
    }

    @Override
    public void setDefaultAddress(AddressBook addressBook) {
        // 获取当前发出请求用户id
        Integer userID = BaseContext.getCurrentId();
        addressBook.setUserId(userID);
        addressBook.setIsDefault(0);
        // 将所有地址的默认状态改为0
        addressBookMapper.updateIsDefaultByUserId(addressBook);
        // 将被选中地址设置为默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
