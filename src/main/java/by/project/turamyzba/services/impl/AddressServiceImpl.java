package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.Address;
import by.project.turamyzba.repositories.AddressRepository;
import by.project.turamyzba.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    @Override
    public List<Address> getChildrenByParentId(Long parentId) {
        return addressRepository.findByParentidOrderByNamekazAsc(parentId);
    }
}
