package com.meditrak.controller;

import com.meditrak.dto.HospitalForm;
import com.meditrak.dto.LoginRequest;
import com.meditrak.dto.WasteEntryForm;
import com.meditrak.service.AuthService;
import com.meditrak.service.MediTrackService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ViewController {
    private final MediTrackService mediTrackService;
    private final AuthService authService;

    public ViewController(MediTrackService mediTrackService, AuthService authService) {
        this.mediTrackService = mediTrackService;
        this.authService = authService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("summary", mediTrackService.dashboardSummary());
        model.addAttribute("routes", mediTrackService.getRoutes());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute LoginRequest loginRequest, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "login";
        }
        if (authService.authenticate(loginRequest)) {
            session.setAttribute("loggedInUser", loginRequest.getUsername());
            return "redirect:/react/dashboard.html";
        }

        model.addAttribute("message", "Invalid credentials. Use admin / meditrak123 for demo.");
        model.addAttribute("loginRequest", loginRequest);
        return "login";
    }

    @GetMapping("/hospitals")
    public String hospitals(Model model) {
        model.addAttribute("hospitalForm", new HospitalForm());
        model.addAttribute("hospitals", mediTrackService.getHospitals());
        return "hospitals";
    }

    @PostMapping("/hospitals")
    public String registerHospital(@Valid @ModelAttribute HospitalForm hospitalForm, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            mediTrackService.registerHospital(hospitalForm);
            model.addAttribute("message", "Hospital registered successfully.");
            model.addAttribute("hospitalForm", new HospitalForm());
        }
        model.addAttribute("hospitals", mediTrackService.getHospitals());
        return "hospitals";
    }

    @GetMapping("/waste-entry")
    public String wasteEntry(Model model) {
        model.addAttribute("wasteEntryForm", new WasteEntryForm());
        model.addAttribute("hospitals", mediTrackService.getHospitals());
        model.addAttribute("wasteEntries", mediTrackService.getWasteEntries());
        return "waste-entry";
    }

    @PostMapping("/waste-entry")
    public String saveWasteEntry(@Valid @ModelAttribute WasteEntryForm wasteEntryForm, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            mediTrackService.addWaste(wasteEntryForm);
            model.addAttribute("message", "Waste entry saved successfully.");
            model.addAttribute("wasteEntryForm", new WasteEntryForm());
        }
        model.addAttribute("hospitals", mediTrackService.getHospitals());
        model.addAttribute("wasteEntries", mediTrackService.getWasteEntries());
        return "waste-entry";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("routes", mediTrackService.getRoutes());
        model.addAttribute("summary", mediTrackService.dashboardSummary());
        model.addAttribute("priorityPickups", mediTrackService.getPriorityWasteEntries());
        return "reports";
    }
}
