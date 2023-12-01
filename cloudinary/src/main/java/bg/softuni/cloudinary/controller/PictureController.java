package bg.softuni.cloudinary.controller;

import bg.softuni.cloudinary.binding.PictureBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PictureController {
    @GetMapping("/pictures/add")
    public String addPicture(){
        return "add";
    }
    @PostMapping("picture/add")
    public String addPicture(PictureBindingModel bindingModel) {
        //todo
        return "redirect:/pictures/all";
    }
    @GetMapping("/pictures/all")
    public String all(){
        return "all";
    }
    @PostMapping("picture/all")
    public String allPictures() {
        //todo
        return "redirect:/pictures/all";
    }
}
