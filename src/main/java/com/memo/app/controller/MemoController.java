package com.memo.app.controller;

import com.memo.app.entity.Memo;
import com.memo.app.entity.User;
import com.memo.app.service.MemoService;
import com.memo.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final UserService userService;

    @GetMapping
    public String listMemos(@RequestParam(value = "filter", required = false) String filter,
                           Authentication authentication,
                           Model model) {
        User user = userService.findByUsername(authentication.getName());
        List<Memo> memos;

        if ("important".equals(filter)) {
            memos = memoService.getImportantMemosByUser(user);
        } else {
            memos = memoService.getAllMemosByUser(user);
        }

        model.addAttribute("memos", memos);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("username", user.getUsername());
        return "memo-list";
    }

    @GetMapping("/new")
    public String newMemoPage(Model model) {
        model.addAttribute("memo", new Memo());
        return "memo-form";
    }

    @PostMapping("/new")
    public String createMemo(@Valid @ModelAttribute("memo") Memo memo,
                            BindingResult result,
                            Authentication authentication) {
        if (result.hasErrors()) {
            return "memo-form";
        }

        User user = userService.findByUsername(authentication.getName());
        memoService.createMemo(memo, user);
        return "redirect:/memos";
    }

    @GetMapping("/edit/{id}")
    public String editMemoPage(@PathVariable Long id,
                              Authentication authentication,
                              Model model) {
        User user = userService.findByUsername(authentication.getName());
        Memo memo = memoService.getMemoById(id);

        if (!memo.getUser().getId().equals(user.getId())) {
            return "redirect:/memos?error=unauthorized";
        }

        model.addAttribute("memo", memo);
        return "memo-form";
    }

    @PostMapping("/edit/{id}")
    public String updateMemo(@PathVariable Long id,
                            @Valid @ModelAttribute("memo") Memo memo,
                            BindingResult result,
                            Authentication authentication) {
        if (result.hasErrors()) {
            return "memo-form";
        }

        User user = userService.findByUsername(authentication.getName());
        memoService.updateMemo(id, memo, user);
        return "redirect:/memos";
    }

    @PostMapping("/delete/{id}")
    public String deleteMemo(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        memoService.deleteMemo(id, user);
        return "redirect:/memos";
    }

    @PostMapping("/toggle-important/{id}")
    public String toggleImportant(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        memoService.toggleImportant(id, user);
        return "redirect:/memos";
    }

    // REST API endpoints for AJAX requests
    @PostMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAjax(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findByUsername(authentication.getName());
            memoService.deleteMemo(id, user);
            response.put("success", true);
            response.put("message", "Memo deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/toggle-important/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleImportantAjax(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findByUsername(authentication.getName());
            Memo memo = memoService.toggleImportant(id, user);
            response.put("success", true);
            response.put("isImportant", memo.getIsImportant());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/new")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createMemoAjax(@Valid @RequestBody Memo memo,
                                                              BindingResult result,
                                                              Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("接收到的 Memo 数据:");
        System.out.println("Title: " + memo.getTitle());
        System.out.println("Content: " + memo.getContent());
        System.out.println("IsImportant: " + memo.getIsImportant());

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Validation failed");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User user = userService.findByUsername(authentication.getName());
            Memo createdMemo = memoService.createMemo(memo, user);
            System.out.println("保存后的 IsImportant: " + createdMemo.getIsImportant());
            response.put("success", true);
            response.put("memo", createdMemo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/edit/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateMemoAjax(@PathVariable Long id,
                                                              @Valid @RequestBody Memo memo,
                                                              BindingResult result,
                                                              Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("更新 Memo ID: " + id);
        System.out.println("接收到的 Memo 数据:");
        System.out.println("Title: " + memo.getTitle());
        System.out.println("Content: " + memo.getContent());
        System.out.println("IsImportant: " + memo.getIsImportant());

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Validation failed");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User user = userService.findByUsername(authentication.getName());
            Memo updatedMemo = memoService.updateMemo(id, memo, user);
            System.out.println("更新后的 IsImportant: " + updatedMemo.getIsImportant());
            response.put("success", true);
            response.put("memo", updatedMemo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
