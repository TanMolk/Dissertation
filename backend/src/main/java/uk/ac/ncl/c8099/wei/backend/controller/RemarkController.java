package uk.ac.ncl.c8099.wei.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.controller.request.AddRemarkRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.ListRemarkRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.RemoveRemarkRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.UpdateRemarkRequest;
import uk.ac.ncl.c8099.wei.backend.dao.RemarkRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Remark;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wei tan
 */

@CrossOrigin
@RestController
@RequestMapping("/remark")
public class RemarkController {

    @Resource
    private RemarkRepository remarkRepository;

    @PostMapping("/add")
    public Boolean add(@RequestBody AddRemarkRequest request) {
        String address = AddressContext.get();

        if (remarkRepository.countByFromAddressAndToAddress(address, request.getTo()) == 0) {
            Remark remark = new Remark();
            remark.setFromAddress(address);
            remark.setToAddress(request.getTo());
            remark.setRemark(request.getRemark());
            remarkRepository.save(remark);
            return remark.getId() != null;
        }
        return false;
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody UpdateRemarkRequest request) {
        String address = AddressContext.get();

        AtomicBoolean haveChange = new AtomicBoolean(false);
        remarkRepository.findById(request.getId()).ifPresent(r -> {
            if (r.getFromAddress().equals(address)) {
                if (remarkRepository.countByFromAddressAndRemark(address, request.getRemark()) == 0) {
                    r.setRemark(request.getRemark());
                    remarkRepository.save(r);
                    haveChange.set(true);
                }
            }
        });

        return haveChange.get();
    }

    @PostMapping("/remove")
    public void remove(@RequestBody RemoveRemarkRequest request) {
        String address = AddressContext.get();
        remarkRepository.findById(request.getId()).ifPresent(r -> {
            if (r.getFromAddress().equals(address)) {
                remarkRepository.deleteById(r.getId());
            }
        });

    }

    @PostMapping("/list")
    public List<Remark> list(@RequestBody ListRemarkRequest request) {
        String address = AddressContext.get();
        return remarkRepository.findRemarkByFromAddress(address);
    }
}
