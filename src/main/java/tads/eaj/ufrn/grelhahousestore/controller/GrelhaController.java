package tads.eaj.ufrn.grelhahousestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tads.eaj.ufrn.grelhahousestore.model.Produtos;
import tads.eaj.ufrn.grelhahousestore.service.FileRepository;
import tads.eaj.ufrn.grelhahousestore.service.GrelhaProdutosService;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class GrelhaController {

    GrelhaProdutosService service;
    FileRepository fileStorageService;
    List<Produtos> cesto = new ArrayList<Produtos>();

    @Autowired
    public void setFileStorageService(FileRepository fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Autowired
    public void setService(GrelhaProdutosService service) {
        this.service = service;
    }
    //ROTA ADMIN
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String getAdmin(Model model) {
        var listaProdutos = service.findAll();
        model.addAttribute("listaProdutos", listaProdutos);
        return "admin";
    }
    //ROTA LISTAR PRODUTO
    @RequestMapping(value = { "/", "/produto" }, method = RequestMethod.GET)
    public String getHome(Model model, HttpServletResponse response) {

        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");
        String currentTime = sdf.format(data);
        Cookie c = new Cookie("visita", currentTime);
        c.setMaxAge(86400);
        response.addCookie(c);

        var listaProdutos = service.findAll();
        model.addAttribute("listaProdutos", listaProdutos);
        return "index";
    }
    //ROTA CADASTRAR PRODUTO
    @RequestMapping("/cadastrar")
    public String getFormCadastrar(Model model) {
        Produtos produto = new Produtos();
        model.addAttribute("produto", produto);
        return "cadastrar";
    }
    //ROTA EDITAR PRODUTO
    @RequestMapping("/editar/{idProduto}")
    public ModelAndView getFormEditar(@PathVariable(name = "idProduto") Long idProduto) {
        ModelAndView modelAndView = new ModelAndView("editar");
        Produtos produtos = service.findById(idProduto);
        modelAndView.addObject("produto", produtos);
        return modelAndView;
    }
    //ROTA DELETAR PRODUTO
    @RequestMapping("/deletar/{idProduto}")
    public String doDelete(@PathVariable(name = "idProduto") Long idProduto) {
        service.delete(idProduto);
        return "redirect:/";
    }
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public String doSalvar(@ModelAttribute @Valid Produtos produto, Errors errors,
                           @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "cadastrar";
        } else {
            produto.setImagem_Uri(UUID.randomUUID().toString() + file.getOriginalFilename());
            service.save(produto);
            fileStorageService.save(file, produto);
            redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");
            return "redirect:/";
        }
    }
    //ROTA ADICIONAR CARRINHO
    @RequestMapping("/addcesto/{idProduto}")
    public String getCarrinho(@PathVariable(name = "idProduto") Long idProduto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("cesto") == null) {
            session.setAttribute("cesto", new ArrayList<Produtos>());
        }
        ArrayList<Produtos> produtosCesto = (ArrayList<Produtos>) session.getAttribute("cesto");
        produtosCesto.add(service.findById(idProduto));
        System.out.println("");
        return "redirect:/";
    }
    //ROTA VER CARRINHO
    @RequestMapping("/cestodecompras")
    public String getListarCesto(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("cesto") == null) {
            return "redirect:/";
        }
        return "cesto";
    }
}
