package by.project.turamyzba.services;

import by.project.turamyzba.entities.Address;

import java.util.List;

public interface AddressService {
    List<Address> getChildrenByParentId(Long parentId);
}
