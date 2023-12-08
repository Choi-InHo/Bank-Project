package com.example.BankProject.loan.controller;

import com.example.BankProject.loan.domain.Judgment;
import com.example.BankProject.loan.dto.*;
import com.example.BankProject.loan.exception.BaseException;
import com.example.BankProject.loan.repository.JudgmentRepository;
import com.example.BankProject.loan.service.ApplicationService;
import com.example.BankProject.loan.service.FileStorageService;
import com.example.BankProject.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.BankProject.loan.dto.ResponseDTO.ok;

@Controller
@Slf4j
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;
    private final JudgmentRepository judgmentRepository;
    private final JudgmentService judgmentService;


    @GetMapping("/list")
    public String getApplicationList(Model model) {
        List<ApplicationDTO.Response> applicationList = applicationService.getAllApplication();
        model.addAttribute("applications", applicationList);
        return "applicationList";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("application", new ApplicationDTO.Request());
        return "createApplicationForm";
    }

    @PostMapping
    public String create(@ModelAttribute("application") ApplicationDTO.Request request, @RequestPart("file") MultipartFile file) {
        ApplicationDTO.Response response = applicationService.create(request);
        Long applicationId = response.getApplicationId();

        if (file != null && !file.isEmpty()) {
            try {
                fileStorageService.save(applicationId, file);
            } catch (BaseException e) {
                log.error("Error while saving file", e);
                // Handle the exception as needed
            }
        }
        return "redirect:/applications/" + applicationId;
    }


    @GetMapping("/{applicationId}")
    public String get(@PathVariable Long applicationId, Model model) {
        ApplicationDTO.Response response = applicationService.get(applicationId);
        model.addAttribute("app", response);

        List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId)
                .map(path -> new FileDTO(path.getFileName().toString(), path.toString()))
                .collect(Collectors.toList());

        // Judgment 정보 가져오기
        JudgmentDTO.Response judgment = judgmentService.getJudgmentOfApplication(applicationId);
        if (judgment != null) {
            // Judgment가 있는 경우, 디테일 부분에서 심사보기 버튼만 표시
            model.addAttribute("hasJudgment", true);
        } else {
            // Judgment가 없는 경우, 디테일 부분에서 심사하기 버튼만 표시
            model.addAttribute("hasJudgment", false);
        }
        model.addAttribute("fileInfos", fileInfos);


        return "applicationDetails";
    }

    @PostMapping("/delete/{applicationId}")
    public String delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return "redirect:/applications/list";
    }

    @GetMapping("/update/{applicationId}")
    public String showUpdateForm(@PathVariable Long applicationId, Model model) {
        ApplicationDTO.Response applicationDetails = applicationService.get(applicationId);
        model.addAttribute("applicationId", applicationId);
        model.addAttribute("applicationDetails", applicationDetails);
        return "updateApplicationForm";
    }

    @PostMapping("/update/{applicationId}")
    public String updateApplication(@PathVariable Long applicationId, @ModelAttribute ApplicationDTO.Request request) {
        applicationService.update(applicationId, request);
        return "redirect:/applications/" + applicationId;
    }

    @GetMapping("/{applicationId}/upload")
    public String showUploadForm(@PathVariable Long applicationId, Model model) {
        model.addAttribute("applicationId", applicationId);
        return "uploadFileForm";
    }

    @PostMapping("/{applicationId}/upload")
    public String uploadFile(@PathVariable Long applicationId, @RequestParam("file") MultipartFile file) throws IOException {
        fileStorageService.save(applicationId, file);
        return "redirect:/applications/" + applicationId;
    }

    @GetMapping("/{applicationId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long applicationId, @RequestParam(value="filename") String filename) throws IOException {
        Resource file = fileStorageService.load(applicationId, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/{applicationId}/files")
    public ResponseDTO<Void> deleteAllFiles(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

    @GetMapping("/{applicationId}/files/info")
    public ResponseDTO<List<FileDTO>> getFileInfos(@PathVariable Long applicationId) {
        List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "downloadFile", applicationId, fileName).build().toString())
                    .build();
        }).collect(Collectors.toList());

        return ok(fileInfos);
    }

    @GetMapping("/{applicationId}/deleteFile")
    public String deleteFile(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return "redirect:/applications/" + applicationId;
    }

    @GetMapping("/contract/{applicationId}")
    public String showContractForm(@PathVariable Long applicationId, Model model) {
        model.addAttribute("applicationId", applicationId);
        return "contractForm";
    }

    @PostMapping("/contract/{applicationId}")
    public String contractApplication(@PathVariable Long applicationId) {
        applicationService.contract(applicationId);
        return "redirect:/applications/" + applicationId;
    }
}
