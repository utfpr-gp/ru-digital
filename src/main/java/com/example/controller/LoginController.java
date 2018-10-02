package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Button;
import com.example.model.Item;
import com.example.model.Product;
import com.example.model.Tag;
import com.example.model.TransactionCredit;
import com.example.model.TransactionDebit;
import com.example.model.User;
import com.example.service.ButtonService;
import com.example.service.ProductService;
import com.example.service.TransactionCreditService;
import com.example.service.TransactionDebitService;
import com.example.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private static CloudHost cloudHost = new CloudHost();

	public LoginController() {

	}

	@Autowired
	private UserService userService;

	@Autowired
	private ButtonService buttonService;

	@Autowired
	private ProductService productService;

	@Autowired
	private TransactionCreditService transactioncreditService;

	@Autowired
	private TransactionDebitService transactiondebitService;

	@RequestMapping(value = { "/teste" }, method = RequestMethod.GET)
	public ModelAndView teste() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("teste");
		return modelAndView;
	}

	@RequestMapping(value = { "/ttt" }, method = RequestMethod.GET)
	public ModelAndView ttt() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ttt");
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/home" }, method = RequestMethod.GET)
	public ModelAndView user(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		model.addAttribute("user", user);
		modelAndView.setViewName("user/home");
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/edit" }, method = RequestMethod.GET)
	public ModelAndView userEdit(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		model.addAttribute("user", user);
		modelAndView.setViewName("user/edit");
		return modelAndView;
	}

	@RequestMapping(value = { "/manager", "/manager/controle" }, method = RequestMethod.GET)
	public ModelAndView Controle(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		// List<Button> buttons = buttonService.findAll();
		Button button = new Button();
		// buttons = buttonService.findAll();
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		User users = new User();
		modelAndView.addObject("usesr", users);

		model.addAttribute("user", user);
		model.addAttribute("button", button);
		// model.addAttribute("buttons", buttons);
		modelAndView.setViewName("manager/controle");
		return modelAndView;
	}

	@RequestMapping(value = { "/manager", "/manager/botoes" }, method = RequestMethod.GET)
	public ModelAndView botoes(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<Button> buttons = buttonService.findAll();
		Button button = new Button();
		buttons = buttonService.findAll();
		model.addAttribute("user", user);
		model.addAttribute("button", button);
		model.addAttribute("buttons", buttons);
		modelAndView.setViewName("manager/botoes");
		return modelAndView;
	}

	@RequestMapping("/check")
	@ResponseBody
	public Button check(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) {
		Button bt = buttonService.getOne(id);
		System.out.println("AAAA" + bt.getValue());
		return bt;
	}

	@RequestMapping("/admin/manager/deletar")
	@ResponseBody
	public void deletarmanager(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) {
		userService.deleteUser(id);

	}

	@RequestMapping("/userexists")
	@ResponseBody
	public boolean userExists(@RequestParam String documento, HttpServletRequest request,
			HttpServletResponse response) {
		User us = userService.findUserByDocument(documento);

		if (us != null && us.getDocument().equals(documento))
			return true;
		return false;
	}

	@RequestMapping("/emailexists")
	@ResponseBody
	public boolean emailExists(@RequestParam String documento, HttpServletRequest request,
			HttpServletResponse response) {
		User us = userService.findUserByEmail(documento);

		if (us != null && us.getEmail().equals(documento))
			return true;
		return false;
	}

	@RequestMapping("/change")
	@ResponseBody
	public void change(@RequestParam long id, @RequestParam String name, @RequestParam BigDecimal value,
			HttpServletRequest request, HttpServletResponse response) {
		Button bt = buttonService.getOne(id);
		bt.setName(name);
		bt.setValue(value);
		buttonService.saveButton(bt);
	}

	@RequestMapping("/deletbutton")
	@ResponseBody
	public void deleteButton(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) {
		Button bt = buttonService.getOne(id);
		buttonService.deletButton(bt);

	}

	@RequestMapping("/findmanager")
	@ResponseBody
	public List<String> findManager(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(id + "MEU ID<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,");
		User user = userService.getOne(id);
		List<String> vec = new ArrayList<String>();
		vec.add(user.getId() + "");
		vec.add(user.getName());
		vec.add(user.getEmail());
		vec.add(user.getDocument());
		return vec;
	}

	@RequestMapping(value = { "/admin", "/manager/botoes/editar" }, method = RequestMethod.GET)
	public ModelAndView botoeseditar(@RequestParam long id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("manager/botoes");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin", "/manager/botoes/novo" }, method = RequestMethod.GET)
	public ModelAndView novoView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("manager/botoes");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin", "/manager/botoes/novo" }, method = RequestMethod.POST)
	public ModelAndView novoBotao(Button button, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		if (button != null && button.getName() != null && button.getValue() != null && button.getName() != "") {
			System.out.println("CHAMDAOAOAOAOAOOAOAOAOOAOAOOAOAOA");
			buttonService.saveButton(button);
		}
		Button b = new Button();
		modelAndView.addObject("mostrar", button);
		List<Button> buttons = buttonService.findAll();
		model.addAttribute("button", b);
		model.addAttribute("buttons", buttons);
		modelAndView.setViewName("manager/botoes");
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/edit/test" }, method = RequestMethod.POST)

	public ModelAndView test(@RequestParam("arquivo") MultipartFile arquivo, Model model) throws IOException {
		System.out.println("ANTES");
		cloudHost.upload(arquivo);
		System.out.println("FIM" + cloudHost.last_public_id);
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		user.setImage(cloudHost.last_public_id);
		userService.updateUser(user);
		String x = cloudHost.getImageUrl(cloudHost.last_public_id);
		System.out.println("OIIIIIIIIIIIIIIIIIIIIIIII" + x);
		model.addAttribute("user", user);
		modelAndView.setViewName("user/edit");
		return modelAndView;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	public static String hashPassword(String password_plaintext) {
		String salt = BCrypt.gensalt(12);
		String hashed_password = BCrypt.hashpw(password_plaintext, salt);

		return (hashed_password);
	}

	@RequestMapping(value = { "/user", "/user/edit" }, method = RequestMethod.POST)
	public ModelAndView userEdited(Model model, @RequestParam String password, @RequestParam String confirm,
			Principal principal) {

		ModelAndView modelAndView = new ModelAndView();
		boolean x = true;
		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		model.addAttribute("user", u);
		if (!password.equals(confirm)) {
			modelAndView.addObject("successMessage", "As senhas nao conferem");
			return modelAndView;
		}
		String pass = hashPassword(password);
		if (password != null && password != "") {
			u.setPassword(pass);
		}
		// userService.saveUser(u);

		model.addAttribute("user", user);
		if (x)
			modelAndView.addObject("successMessage", "Conta alterada com sucesso");

		modelAndView.setViewName("user/edit");
		userService.updateUser(u);

		return modelAndView;
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}

	public boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
		if (hasUserRole)
			return true;
		return false;
	}

	public boolean isUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
			System.out.println("EPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		} else {
			System.out.println(
					"NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNEPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

		}
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("USER"));
		if (hasUserRole)
			return true;
		return false;
	}

	public boolean isManager() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
		if (hasUserRole)
			return true;
		return false;
	}

	@RequestMapping("/finduser")
	@ResponseBody
	public String findUser(@RequestParam String x) {
		System.out.println("...");
		User user = userService.findUserByEmail(x);
		User up;
		if (user == null) {
			up = userService.findUserByDocument(x);
			user = up;
		}
		return user.getImage();
	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login(Principal principal, HttpServletRequest request, HttpSession session) {
		if (principal == null) {
			System.out.println("ME CHAMOU ASSIM");
			return "login";
		}

		System.setProperty("userDetails", principal.getName());
		if (isUser())
			return "redirect:" + "/user";
		if (isAdmin())
			return "redirect:" + "/admin/home";
		if (isManager())
			return "redirect:" + "/manager";
		return "";
	}

	@RequestMapping(value = { "/manager/products" }, method = RequestMethod.GET)
	public ModelAndView ManagerProducts() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("manager/products");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();

		Boolean var = true;
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, @RequestParam String hid,
			@RequestParam String confirm) {
		System.out.println("AaaAaAaaAaa" + hid);
		System.out.println("BbbbBbBBBbBbbBBb" + user.getDocument());
		user.setDocument(hid);
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		User raExists = userService.findUserByDocument(user.getDocument());
		String str = user.getDocument().replace(",", "");
		str = str.replace(".", "");
		str = str.replace("-", "");
		user.setDocument(str);
		if (!user.getPassword().equals(confirm)) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
			modelAndView.setViewName("registration");
			modelAndView.addObject("user", new User());
			return modelAndView;

		}
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}

		if (raExists != null) {
			bindingResult.rejectValue("document", "error.user",
					"There is already a user registered with the email provided");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			user.setImage("rejoed05uyghymultqsv");
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/registermanager", method = RequestMethod.GET)
	public ModelAndView regis(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.addObject("result", user.getName());
		List<User> users = userService.findManager();
		model.addAttribute("managers", users);
		modelAndView.setViewName("admin/registermanager");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/product", method = RequestMethod.GET)
	public ModelAndView product() {
		ModelAndView modelAndView = new ModelAndView();
		Product product = new Product();
		modelAndView.addObject("product", product);
		modelAndView.addObject("products", productService.findAll());
		modelAndView.setViewName("admin/product");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/lancamentos", method = RequestMethod.GET)
	public ModelAndView lancar() {
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("products", productService.findAll());
		Product product = productService.findByName("comida");
		modelAndView.addObject("product", product);
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("admin/lancamentos");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/product", method = RequestMethod.POST)
	public ModelAndView newProduct(@Valid Product product) {
		ModelAndView modelAndView = new ModelAndView();

		Product productExists = productService.findByName(product.getName());
		productService.saveProduct(product);
		modelAndView.addObject("successMessage", "Produto Cadastrado com Sucesso");
		modelAndView.addObject("product", new Product());
		modelAndView.addObject("products", productService.findAll());
		modelAndView.setViewName("admin/product");
		return modelAndView;
	}

	@RequestMapping(value = "/manager/lancar", method = RequestMethod.POST)
	public void lancar(@RequestParam(value = "my[]") int[] my, @RequestParam("k") BigDecimal k) {
		System.out.println("Ola");
		System.out.println(k);
		List<Product> ls = null;
		TransactionDebit td = new TransactionDebit();
		for (int i = 0; i < my.length; i++) {
			System.out.println("[]" + i + " " + my[i]);
			Product p = new Product();
			p.setId(my[i]);
			ls.set(i, p);
		}
		td.setProdutos(ls);
		td.setValue(k);
		transactiondebitService.saveTransaction(td);

	}

	@RequestMapping(value = "/manager/buscar", method = RequestMethod.GET)
	public String searchuser(User u, Model model, @RequestParam("email") String email) {
		User us = userService.findUserByEmail(email);
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		if (us != null) {
			model.addAttribute("users", us);
		}
		if (us == null) {
			User use = userService.findUserByDocument(email);
			if (use != null) {
				model.addAttribute("users", use);
				List<Button> buttons = buttonService.findAll();
				Button button = new Button();
				buttons = buttonService.findAll();
				model.addAttribute("buttons", buttons);

			} else
				model.addAttribute("usernotfound", "verdade");
		}

		return "manager/controle";
	}

	private static final String AJAX_HEADER_NAME = "X-Requested-With";
	private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

	@PostMapping(params = "addItem", path = { "/manager/lancamentos", "/manager/lancamentos/{id}" })
	public String addOrder(Product products, HttpServletRequest request) {
		products.items.add(new Item());
		if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
			// It is an Ajax request, render only #items fragment of the page.
			return "/manager/lancamentos::#items";
		} else {
			// It is a standard HTTP request, render whole page.
			return "/manager/lancamentos";
		}
	}

	// "removeItem" parameter contains index of a item that will be removed.
	@PostMapping(params = "removeItem", path = { "/admin/manager", "/admin/manager/{id}" })
	public String removeOrder(Product products, @RequestParam("removeItem") int index, HttpServletRequest request) {
		products.items.remove(index);
		if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
			return "/manager/lancamentos::#items";
		} else {
			return "/manager/lancamentos";
		}
	}

	@RequestMapping(value = "/manager/consumo", method = RequestMethod.GET)
	public String getConsumo() {
		return "manager/controle";
	}

	@RequestMapping(value = "/manager/insertcredit", method = RequestMethod.GET)
	public String getCredits() {
		return "manager/controle";
	}

	@RequestMapping(value = "/manager/consumo", method = RequestMethod.POST)
	public String consumo(@RequestParam("iduser") long iduser, @RequestParam("idbutton") long idbutton,
			@RequestParam("debitovalue") BigDecimal debito, @RequestParam("description") String description,
			Model model, RedirectAttributes redir) {
		User user = userService.getOne(iduser);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		TransactionCredit transactioncredit = new TransactionCredit();
		if (debito != null) {
			System.out.println("O ERRO ESTA ABAIXO");
		}
		if (debito == null) {

			Button button = buttonService.getOne(idbutton);
			if (user.getBalance() != null && button.getValue() != null
					&& (user.getBalance().compareTo(button.getValue()) > 0
							|| user.getBalance().compareTo(button.getValue()) == 0)) {
				transactioncredit.setButton(button);
				BigDecimal val = button.getValue();
				val = button.getValue().negate();
				transactioncredit.setValue(val);
				transactioncredit.setUser(user);
				transactioncredit.setOperator(username);
				BigDecimal total = user.getBalance().add(val);
				user.setBalance(total);
				userService.updateUser(user);
				transactioncreditService.saveTransaction(transactioncredit);
				model.addAttribute("status", "Consumo lanncado!");
				redir.addFlashAttribute("status2", "Consumo lanncado!");
			} else {
				model.addAttribute("status", "Saldo Insuficiente!");
			}

		} else {
			Button b = new Button();
			if ((user.getBalance() != null && debito != null)
					&& (debito.compareTo(user.getBalance()) < 0 || debito.compareTo(user.getBalance()) == 0)) {
				if (description.equals("")) {
					b.setName("Outro Valor");
				} else {
					b.setName(description);
				}
				System.out.println("DESCRIPTION" + description);
				b.setValue(debito);
				b.setOutros(true);
				buttonService.saveButton(b);
				System.out.println(b.getId());
				BigDecimal val = debito.negate();
				transactioncredit.setButton(b);
				transactioncredit.setValue(val);
				transactioncredit.setUser(user);
				transactioncredit.setOperator(username);
				BigDecimal total = user.getBalance().add(val);
				user.setBalance(total);
				userService.updateUser(user);
				transactioncreditService.saveTransaction(transactioncredit);
				model.addAttribute("status", "Consumo lanncado!");
				redir.addFlashAttribute("status2", "Consumo lanncado!");
			} else {
				model.addAttribute("status", "Saldo Insuficiente!");
			}
			System.out.println("VALORRR" + debito);
			System.out.println("VALORRR" + description);
		}
		return searchuser(user, model, user.getDocument());

	}

	@RequestMapping(value = "/manager/insertcredit", method = RequestMethod.POST)
	public String insertcredit(User u, Model model, @RequestParam("users.document") String document,
			@RequestParam("balance") BigDecimal balance, Principal principal) {
		User us = userService.findUserByDocument(document);
		if (us == null) {
			User user = userService.findUserByEmail(document);
			us = user;
		}
		TransactionCredit transactioncredit = new TransactionCredit();
		transactioncredit.setValue(balance);
		if (us.getBalance() != null) {
			balance = balance.add(us.getBalance());
		}
		us.setBalance(balance);
		transactioncredit.setUser(us);
		transactioncredit.setOperator(principal.getName());
		model.addAttribute("status", "Créditos Inseridos!");
		userService.updateUser(us);
		transactioncreditService.saveTransaction(transactioncredit);
		model.addAttribute("users", us);
		List<Button> buttons = buttonService.findAll();
		model.addAttribute("buttons", buttons);
		System.out.println("PRINCIPALLLLLLLLLLL" + principal.getName());

		return searchuser(us, model, us.getDocument());
	}

	@RequestMapping(value = "/manager/creditos", method = RequestMethod.GET)
	public ModelAndView getchuser() {
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);

		modelAndView.setViewName("manager/creditos");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/teste", method = RequestMethod.GET)
	public ModelAndView testeUser() {
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);

		modelAndView.setViewName("admin/teste");
		return modelAndView;
	}

	@RequestMapping(value = "/manager/getlist", method = RequestMethod.GET)
	public @ResponseBody List<Tag> getTags(@RequestParam String tagName) {
		System.out.println("OLAAAA" + tagName);
		return simulateSearchResult(tagName);

	}

	@RequestMapping(value = "/suggestion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Tag> autocompleteSuggestions(@RequestParam("searchstr") String searchstr) {
		System.out.println("OLAAAA");
		return simulateSearchResult(searchstr);
	}

	List<Tag> aux = new ArrayList<Tag>();

	private User aux2;

	private List<Tag> simulateSearchResult(String tagName) {
		List<Tag> result = new ArrayList<Tag>();
		// iterate a list and filter by tagName
		for (Tag tag : aux) {
			if (tag.getTagName().contains(tagName)) {
				result.add(tag);
			}
		}
		return result;
	}

	@RequestMapping(value = "/admin/registermanager", method = RequestMethod.POST)
	public ModelAndView createNewManager(@Valid User user, BindingResult bindingResult, Model model,
			@RequestParam("confirm") String confirm) {
		ModelAndView modelAndView = new ModelAndView();

		if (user.getId() != -1) {
			modelAndView.addObject("successMessage", "editado");

			userService.updateUser(user);
			List<User> users = userService.findManager();
			model.addAttribute("managers", users);
			modelAndView.setViewName("admin/registermanager");
		} else {
			if (!user.getPassword().equals(confirm)) {
				bindingResult.rejectValue("password", "error.user", "Senhas não conferem");
			}
			User userExists = userService.findUserByEmail(user.getEmail());
			User userExists2 = userService.findUserByDocument(user.getDocument());
			if (userExists != null) {
				bindingResult.rejectValue("email", "error.user", "Email ja esta sendo usado por outro usuario");
			}
			if (userExists2 != null) {
				bindingResult.rejectValue("document", "error.user", "Documento ja cadastrado no sistema");
			}
			if (bindingResult.hasErrors()) {
				List<User> users = userService.findManager();
				model.addAttribute("managers", users);
				modelAndView.setViewName("admin/registermanager");
			} else {
				System.out.println("---------------------------------------------------------------");
				userService.saveManager(user);
				modelAndView.addObject("successMessage", "cadastrado");
				modelAndView.addObject("user", new User());
			}
			List<User> users = userService.findManager();
			model.addAttribute("managers", users);
			modelAndView.setViewName("admin/registermanager");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/manager/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("manager/home");
		return modelAndView;
	}

}
