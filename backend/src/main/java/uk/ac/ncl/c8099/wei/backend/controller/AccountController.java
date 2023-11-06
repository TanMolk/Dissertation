package uk.ac.ncl.c8099.wei.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.c8099.wei.backend.constants.AccountConstant;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.controller.request.ListAccountRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.LoginRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.SignInRequest;
import uk.ac.ncl.c8099.wei.backend.controller.response.ListAccountResponse;
import uk.ac.ncl.c8099.wei.backend.dao.AccountRepository;
import uk.ac.ncl.c8099.wei.backend.dao.RemarkRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Account;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Remark;
import uk.ac.ncl.c8099.wei.backend.service.AccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wei tan
 */

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountRepository accountRepository;
    @Resource
    private RemarkRepository remarkRepository;

    @PostMapping("/sign-up")
    public Boolean signUp(@RequestBody SignInRequest request) {
        if (AccountConstant.SIGN_UP_CIPHER.equals(request.getCipher())) {
            return accountService.addAccount(AddressContext.get());
        }
        return false;
    }

    @PostMapping("/login")
    public Boolean login(@RequestBody LoginRequest request) {
        if (AccountConstant.LOGIN_CIPHER.equals(request.getCipher())) {
            return accountService.containAddress();
        }
        return false;
    }

    @PostMapping("/list")
    public List<ListAccountResponse> list(@RequestBody ListAccountRequest request) {
        String requestAddress = AddressContext.get();
        List<Account> account = accountRepository.findByAddress(requestAddress);
        Integer currentAccountId;

        if (account.size() == 1) {
            currentAccountId = account.get(0).getId();
        } else {
            currentAccountId = null;
        }

        List<Account> accounts = accountRepository.findAll();

        Map<String, String> remarks;
        if (currentAccountId != null) {
            remarks = remarkRepository.findRemarkByFromAddress(requestAddress)
                    .stream().collect(Collectors.toMap(Remark::getToAddress, Remark::getRemark));
        } else {
            remarks = new HashMap<>();
        }

        List<ListAccountResponse> result = new ArrayList<>();
        accounts.forEach(ac -> {
            if (!ac.getId().equals(currentAccountId)) {
                ListAccountResponse response = new ListAccountResponse();
                String address = ac.getAddress();
                response.setValue(address);
                String label = remarks.get(address);
                response.setLabel(label == null ? address : label);
                result.add(response);
            }
        });
        return result;
    }

}
