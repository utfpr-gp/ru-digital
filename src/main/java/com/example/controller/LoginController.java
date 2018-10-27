package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Button;
import com.example.model.Company;
import com.example.model.PasswordResetToken;
import com.example.model.Tag;
import com.example.model.TransactionCredit;
import com.example.model.User;
import com.example.model.UserBalance;
import com.example.repository.ButtonRepository;
import com.example.repository.CompanyRepository;
import com.example.repository.TokenRepository;
import com.example.repository.TransactionCreditRepository;
import com.example.repository.UserBalanceRepository;
import com.example.repository.UserRepository;
import com.example.service.ButtonService;
import com.example.service.CompanyService;
import com.example.service.TokenService;
import com.example.service.TransactionCreditService;
import com.example.service.UserBalanceService;
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
	private CompanyService companyService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ButtonService buttonService;

	@Autowired
	private TransactionCreditService transactioncreditService;

	@Autowired
	private UserBalanceService userbalanceService;

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

	@Autowired
	TransactionCreditRepository transactionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ButtonRepository buttonRepository;

	@RequestMapping("/checkpages")
	@ResponseBody
	public BigInteger numberpages(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("LOGADO ESTa");
		BigInteger x = transactioncreditService.totalTransactions();
		System.out.println("MEU X AMIGO" + x);
		return x;
	}

	@RequestMapping(value = { "/user", "/user/extrato/geral" }, method = RequestMethod.POST)
	public ModelAndView userbyPageGeral(Model model, @RequestParam(value = "pages") int page,
			@RequestParam(value = "op") String op, @RequestParam(value = "ini") String ini,
			@RequestParam(value = "fim") String fim,

			@RequestParam(value = "empresa") long empresa, Principal principal) {
		PageRequest pageRequest = new PageRequest(page - 1, 5, Sort.Direction.valueOf("ASC"), "data");
		ModelAndView modelAndView = new ModelAndView();
		Page<TransactionCredit> t = null;

		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		System.out.println("/user/extrato/geral");
		long size = 0;
		if (op.equals("tempocredito")) {
			long inicio = Long.valueOf(ini).longValue();
			long ultimo = Long.valueOf(fim).longValue();
			t = this.transactionRepository.findByTc(inicio, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTc(inicio, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			System.out.println("/user/extrato/geral             IF1");
			model.addAttribute("tipo", "tempodebito");

		} else if (op.equals("tempodebito")) {
			long inicio = Long.valueOf(ini).longValue();
			long ultimo = Long.valueOf(fim).longValue();
			t = this.transactionRepository.findByTd(inicio, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTd(inicio, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			System.out.println("/user/extrato/geral             IF2");
			model.addAttribute("tipo", "tempocredito");

		} else if (op.equals("credito")) {
			t = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("tipo", "credito");
			System.out.println("/user/extrato/geral             IF3");
			System.out.println("FITRO DE CREDITO");
		} else if (op.equals("debito")) {
			t = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("tipo", "debito");
			System.out.println("/user/extrato/geral             IF4");
			System.out.println("FITRO DE DEBITO");
		} else if (op.equals("data")) {
			long inicial = Long.valueOf(ini).longValue();
			System.out.println("FITRO DE DAATA");
			long ultimo = Long.valueOf(fim).longValue();
			model.addAttribute("tipo", "data");
			System.out.println("/user/extrato/geral             IF5");
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			t = this.transactionRepository.findByDate(inicial, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByDate(inicial, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
		} else {
			t = this.transactionRepository.findAllUser(u.getId(), pageRequest);
			System.out.println("/user/extrato/geral             IF6");
			size = this.transactionRepository.findAllUser(u.getId(), pageRequest).getTotalElements();
		}
		if (ini == null || ini.equals("")) {
			ini = ".";
		}
		if (fim == null || fim.equals("")) {
			fim = ".";
		}
		model = params(model, u);
		model.addAttribute("transactions", t);
		model.addAttribute("size", size);
		modelAndView.setViewName("redirect:geral?page=" + page + "&op=" + op + "&size=" + size + "&ini=" + ini + "&fim="
				+ fim + "&empresa=" + empresa);
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/extrato/geral" }, method = RequestMethod.GET)
	public ModelAndView userbyPageGeralGet(Model model, @RequestParam(value = "page") int page,
			@RequestParam(value = "op") String op, @RequestParam(value = "size") long size,
			@RequestParam(value = "ini") String ini, @RequestParam(value = "fim") String fim,
			@RequestParam(value = "empresa") long empresa, Principal principal) {
		PageRequest pageRequest = new PageRequest(page - 1, 5, Sort.Direction.valueOf("ASC"), "data");
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("WARD");
		Page<TransactionCredit> t = null;
		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		if (op.equals("tempocredito")) {
			long inicio = Long.valueOf(ini).longValue();
			long ultimo = Long.valueOf(fim).longValue();
			System.out.println("/user/extrato/geral             PRIMEIRO CASO");
			t = this.transactionRepository.findByTc(inicio, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTc(inicio, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempocredito");

		} else if (op.equals("tempodebito")) {
			long inicio = Long.valueOf(ini).longValue();
			long ultimo = Long.valueOf(fim).longValue();
			t = this.transactionRepository.findByTd(inicio, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTd(inicio, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			model.addAttribute("inicial", ini);
			System.out.println("/user/extrato/geral             SEGUNDO CASO");

			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempodebito");

		} else if (op.equals("credito")) {
			t = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("tipo", "credito");
			System.out.println("/user/extrato/geral             TERCEIRO CASO");
			System.out.println("PAGE" + page);
			System.out.println("OP" + op);
			System.out.println("INI" + ini);
			System.out.println("FIM" + fim);
			System.out.println("EMPRESA" + empresa);
			System.out.println("FITRO DE CREDITO");
		} else if (op.equals("debito")) {
			t = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("tipo", "debito");
			System.out.println("/user/extrato/geral             QUARTO CASO");
			System.out.println("FITRO DE DEBITO");
		} else if (op.equals("data")) {
			long inicial = Long.valueOf(ini).longValue();
			System.out.println("FITRO DE DAATA");
			long ultimo = Long.valueOf(fim).longValue();
			model.addAttribute("tipo", "data");
			System.out.println("/user/extrato/geral             QUINTO CASO");
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			t = this.transactionRepository.findByDate(inicial, ultimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByDate(inicial, ultimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
		}

		else {
			t = this.transactionRepository.findAllUser(u.getId(), pageRequest);
			System.out.println("/user/extrato/geral             SEXTO CASO");
			size = this.transactionRepository.findAllUser(u.getId(), pageRequest).getTotalElements();
		}
		model = params(model, u);
		model.addAttribute("transactions", t);
		model.addAttribute("filtro", "filtrogeral");
		model.addAttribute("size", size);
		model.addAttribute("empresa", empresa);
		modelAndView.setViewName("user/extrato");
		return modelAndView;
	}

	public long yearToDateInitial(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		long date = cal.getTimeInMillis();
		return date;
	}

	public long yearToDateEnd(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11); // 11 = december
		cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
		long date = cal.getTimeInMillis();
		return date;
	}

	@RequestMapping(value = { "/user", "/user/extrato/filterpage" }, method = RequestMethod.POST)
	public ModelAndView userbyFilter(Model model, @RequestParam(value = "filtro") String filtro,
			@RequestParam(value = "filtroano") String filtroano, @RequestParam(value = "empresa") long empresa,
			@RequestParam(value = "filtromes") String filtromes, Principal principal) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>" + filtro);
		System.out.println(">>>>>>>>>>>>>>>>>>>>MES" + filtromes);
		System.out.println(">>>>>>>>>>>>>>>>>>>>ANO" + filtroano);
		List<Company> company = companyRepository.findAll();
		model.addAttribute("company", company);
		model.addAttribute("company", company);
		model.addAttribute("filtros", "ok");

		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		long ini = 0, fim = 0;
		if (!filtromes.equals("0") && !filtroano.equals("0")) {
			String date = filtromes + "/1/" + filtroano;
			LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
			ini = convertedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
			convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
			System.out.println(">>>>>>>>>>>>>>>>>>>>ULTIMODIADOMES" + convertedDate);
			fim = convertedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
			fim = fim + 86400000;
			if (filtromes.equals("12")) {
				int numero = Integer.parseInt(filtroano);
				String datefim = "1" + "/1/" + numero;
				LocalDate convertedDate2 = LocalDate.parse(datefim, DateTimeFormatter.ofPattern("M/d/yyyy"));
				fim = convertedDate2.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
			}
			long tot = fim - ini;
			System.out.println(">>>>>>>>>>>>>>>>>>>>MILIS" + tot);
		}
		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "data");
		ModelAndView modelAndView = new ModelAndView();
		Page<TransactionCredit> t = null;
		model.addAttribute("empresa", empresa);
		long size = 0;
		if (filtro.equals("NOTFOUND")) {
			t = null;
			size = 0;
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempocredito");

		} else if (filtro.equals("ANOC")) {
			int ano = Integer.parseInt(filtroano);
			System.out.println("ANO JA CONVERTIDO" + ano);
			long miliInicial = yearToDateInitial(ano);
			long miliUltimo = yearToDateEnd(ano);
			System.out.println("MILESEGUNDOS INICIAL!!!!!" + miliInicial);
			System.out.println("MILESEGUNDOS FINAL!!!!!" + miliUltimo);
			t = this.transactionRepository.findByTc(miliInicial, miliUltimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTc(miliInicial, miliUltimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			;
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempocredito");

		} else if (filtro.equals("ANOD")) {
			int ano = Integer.parseInt(filtroano);
			System.out.println("ANO JA CONVERTIDO" + ano);
			long miliInicial = yearToDateInitial(ano);
			long miliUltimo = yearToDateEnd(ano);
			System.out.println("MILESEGUNDOS INICIAL!!!!!" + miliInicial);
			System.out.println("MILESEGUNDOS FINAL!!!!!" + miliUltimo);
			t = this.transactionRepository.findByTd(miliInicial, miliUltimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTd(miliInicial, miliUltimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			;
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempodebito");
		} else if (filtro.equals("ANOCD")) {
			int ano = Integer.parseInt(filtroano);
			System.out.println("ANO JA CONVERTIDO" + ano);
			long miliInicial = yearToDateInitial(ano);
			long miliUltimo = yearToDateEnd(ano);
			System.out.println("MILESEGUNDOS INICIAL!!!!!" + miliInicial);
			System.out.println("MILESEGUNDOS FINAL!!!!!" + miliUltimo);
			t = this.transactionRepository.findByDate(miliInicial, miliUltimo, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByDate(miliInicial, miliUltimo, empresa, u.getId(), pageRequest)
					.getTotalElements();
			;
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempodebito");
		}

		else if (filtro.equals("TC")) {
			t = this.transactionRepository.findByTc(ini, fim, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTc(ini, fim, empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempocredito");

		} else if (filtro.equals("TD")) {
			t = this.transactionRepository.findByTd(ini, fim, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByTd(ini, fim, empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
			model.addAttribute("tipo", "tempodebitoo");

		} else if (filtro.equals("C")) {
			t = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValuePostive(empresa, u.getId(), pageRequest).getTotalElements();

			model.addAttribute("tipo", "credito");
		} else if (filtro.equals("D")) {
			t = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByValueNegative(empresa, u.getId(), pageRequest).getTotalElements();
			model.addAttribute("tipo", "debito");
		} else if (filtro.equals("T")) {
			t = this.transactionRepository.findByDate(ini, fim, empresa, u.getId(), pageRequest);
			size = this.transactionRepository.findByDate(ini, fim, empresa, u.getId(), pageRequest).getTotalElements();
			System.out.println("TODO");
			model.addAttribute("tipo", "data");
			model.addAttribute("inicial", ini);
			model.addAttribute("final", fim);
		} else {
			t = this.transactionRepository.findAllUser(u.getId(), pageRequest);
			size = this.transactionRepository.findAllUser(u.getId(), pageRequest).getTotalElements();
		}
		// model = params(model, user);
		System.out.println("MEU SIZEEEEEEEEEEEEE" + size);
		// model = params(model, user);
		model = params(model, u);
		model.addAttribute("transactions", t);
		model.addAttribute("size", size);
		model.addAttribute("filtro", "filterpage");
		modelAndView.setViewName("user/extrato");

		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/extrato" }, method = RequestMethod.GET)
	public ModelAndView userExtrato(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		List<TransactionCredit> transactioncredit = transactioncreditService.findAll();
		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "data");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Page<TransactionCredit> tall = this.transactionRepository.findAllUser(user.getId(), pageRequest);

		model.addAttribute("transactions", tall);
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		List<String> buttons = transactioncreditService.listdiferentButtons();
		UserBalance ub = userbalanceRepository.hasBalance(user.getId(), 1);
		List<User> us = userService.findManager();
		model.addAttribute("operators", us);
		model.addAttribute("buttons", buttons);
		model = params(model, user);
		model.addAttribute("user", user);

		modelAndView.setViewName("user/extrato");
		return modelAndView;
	}

	public Model params(Model model, User user) {
		List<Company> company = companyRepository.findAll();
		model.addAttribute("company", company);
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		UserBalance ub = userbalanceRepository.hasBalance(user.getId(), 1);

		BigDecimal b = (userbalanceRepository.sumBalances(user.getId()));
		if (b != null) {
			String stot = nf.format(b);
			model.addAttribute("somatotal", stot);
		}
		if (ub != null) {
			String s = nf.format(ub.getBalance());
			model.addAttribute("valor", s);
		}
		return model;
	}

	@RequestMapping(value = { "/user", "/user/extrato/filterpage" }, method = RequestMethod.GET)
	public ModelAndView getFilterExtrato(Model model, Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		List<TransactionCredit> transactioncredit = transactioncreditService.findAll();
		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "data");

		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		Page<TransactionCredit> tall = this.transactionRepository.findAllUser(u.getId(), pageRequest);

		model.addAttribute("transactions", tall);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		List<Company> company = companyRepository.findAll();
		List<String> buttons = transactioncreditService.listdiferentButtons();
		UserBalance ub = userbalanceRepository.hasBalance(user.getId(), 1);
		List<User> us = userService.findManager();
		BigDecimal b = (userbalanceRepository.sumBalances(user.getId()));
		if (b != null) {
			String stot = nf.format(b);
			model.addAttribute("somatotal", stot);
		}
		model.addAttribute("operators", us);
		model.addAttribute("company", company);
		model.addAttribute("filtros", "ok");
		model = params(model, u);
		model.addAttribute("buttons", buttons);
		if (ub != null) {
			String s = nf.format(ub.getBalance());
			model.addAttribute("valor", s);
		}
		model.addAttribute("user", user);
		modelAndView.setViewName("user/extrato");
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/extrato/filtro" }, method = RequestMethod.GET)
	public ModelAndView userExtratoFiltro(Model model, @RequestParam("name") String nome,
			@RequestParam("dataini") String dataini, @RequestParam("datafim") String datafim,
			@RequestParam("operator") String operador, @RequestParam("type") String tipo) throws ParseException {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("OI FILTROOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		System.out.println("OI " + nome);
		System.out.println("OI " + operador);

		String pars;
		if (nome == null || nome == "" || nome.equals("")) {
			pars = "0";
		} else
			pars = "1";
		if (operador == null || operador == "" || operador.equals("")) {
			pars = pars + "0";
		} else
			pars = pars + "1";
		if (dataini == null || dataini == "" || dataini.equals("")) {
			pars = pars + "0";
		} else
			pars = pars + "1";
		if (datafim == null || datafim == "" || datafim.equals("")) {
			pars = pars + "0";
		} else
			pars = pars + "1";
		if (tipo == null || tipo == "Credito e Debito" || tipo.equals("Credito e Debito")) {
			pars = pars + "0";
		} else
			pars = pars + "1";
		String[] nomes = nome.split("\\,", -1);
		ArrayList<String> nomesList = new ArrayList<String>();
		for (String s : nomes) {
			nomesList.add(s);
		}
		String[] op = operador.split("\\,", -1);
		ArrayList<String> operadorList = new ArrayList<String>();
		for (String s : op) {
			operadorList.add(s);
		}
		System.out.println("OI " + dataini);
		System.out.println("OI " + operador);
		System.out.println("OI " + tipo);
		List<TransactionCredit> transactioncredit = transactioncreditService.listFilter(nomesList, operadorList,
				dataini, datafim, tipo, pars);
		model.addAttribute("transactions", transactioncredit);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<String> buttons = transactioncreditService.listdiferentButtons();
		List<User> us = userService.findManager();
		BigDecimal b = userbalanceRepository.sumBalances(user.getId());
		if (b != null) {
			String stot = nf.format(b);

			model.addAttribute("somatotal", stot);
		}
		model.addAttribute("operators", us);
		model.addAttribute("buttons", buttons);
		model.addAttribute("user", user);
		modelAndView.setViewName("user/extrato");
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
		Button button = new Button();
		model.addAttribute("user", user);
		model.addAttribute("button", button);

		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "name");
		Page<Button> buts = buttonRepository.activeButtons(pageRequest);
		model.addAttribute("buttonsme", buts);
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
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("NA TENTATIVA DE DELETAR O BOTAO");
		modelAndView.addObject("mostrardel", bt);
		buttonService.deletButton(bt);

	}

	@RequestMapping("/checarpin")
	@ResponseBody
	public boolean checarPin(@RequestParam String id, @RequestParam int pin, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("AQUIIIIIII" + pin);
		System.out.println("AQUIIIIIII" + id);
		User u = userService.findUserByDocument(id);
		if (u.getPin() == pin)
			return true;
		return false;

	}

	@RequestMapping("/checarsaldoempresa")
	@ResponseBody
	public String checarSaldoEmpresa(@RequestParam long empresa, HttpServletRequest request,
			HttpServletResponse response, Principal principal, Model model) {
		User u = userService.findUserByDocument(principal.getName());
		if (u == null) {
			User us = userService.findUserByEmail(principal.getName());
			u = us;
		}
		UserBalance ub = userbalanceRepository.hasBalance(u.getId(), empresa);
		String s = "";
		if (ub != null && ub.getBalance() != null) {
			System.out.println("MEU UB BALANCE" + ub.getBalance());
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			s = nf.format(ub.getBalance());
			model.addAttribute("valor", s);
		}
		return s;

	}

	@RequestMapping("/cechar")
	@ResponseBody
	public int novoPin(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) {
		User u = userService.getOne(id);
		u.generatePin();
		userService.updateUser(u);
		return u.getPin();
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

	@Autowired
	TokenRepository tokenRepository;

	@Autowired
	UserBalanceRepository userbalanceRepository;

	@RequestMapping(value = { "/login/senha" }, method = RequestMethod.POST)
	public ModelAndView novaSenha(Model model, @RequestParam(value = "email") String email) throws MessagingException {
		ModelAndView modelAndView = new ModelAndView();
		User u = userService.findUserByEmail(email);
		System.out.println("ENVIAR O EMAIL E SALVAR TOKEN NO BANCO");
		String token = "";
		if (u == null)
			model.addAttribute("resposta", "nencontrado");
		else {
			System.out.println("ANTES");
			PasswordResetToken x = tokenRepository.hasToken(u.getId());
			System.out.println("DEPOIS");
			if (x == null || x.isExpired()) {
				System.out.println("DENTRO DO IF");
				token = generateToken();
				token = bCryptPasswordEncoder.encode(token);
				PasswordResetToken t = tokenService.findByToken(token);
				while (t != null) {
					token = generateToken();
					t = tokenService.findByToken(token);
				}
				PasswordResetToken reset = new PasswordResetToken();
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24 * 2));
				reset.setExpiryDate(tomorrow);
				reset.setExpiryDate(1440);
				reset.setToken(token);
				reset.setUser(u);
				tokenService.saveToken(reset);
			} else
				token = x.getToken();
			sendMailForgetPass(u, token);
			model.addAttribute("resposta", "encontrado");
		}
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@Autowired
	private JavaMailSender mailSender;

	public String sendMailForgetPass(User u, String rash) throws MessagingException {
		SimpleMailMessage message = new SimpleMailMessage();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "Olá " + u.getName() + "\n Você solicitou mudança de senha no sistema UTFCOIN. <br> \n "
				+ "Para recuperar a sua senha click no link abaixo: <br> "
				+ " <a href='http://localhost:8030/reset?token=" + rash + "&email=" + u.getEmail()
				+ " '  >Resetar senha</a>  <br> <br> "
				+ "Caso não tenha solicitado alteração, ignore essa mensagem e nos informe."
				+ "<br> Atenciosamente, UTFCOIN";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(u.getEmail());
		helper.setSubject("UTFCOIN - Solicitação de nova senha");
		helper.setFrom("utfcoin@gmail.com");

		try {
			mailSender.send(mimeMessage);
			return "jsonTemplate";
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao enviar email.";
		}
	}

	public String generateToken() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		String token = bytes.toString();
		return token;
	}

	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = { "/reset" }, method = RequestMethod.GET)
	public String displayResetPasswordPage(@RequestParam(value = "token") String token,
			@RequestParam(value = "email") String email, Model model) {
		System.out.println("AQUIIIIII" + token);
		PasswordResetToken resetToken = tokenService.findByToken(token);

		System.out.println("TA AQUI MEU ID");
		if (resetToken == null || resetToken.getId() == null) {
			System.out.println("AH BLZ");
			model.addAttribute("error", "Token não encontrado");
		} else if (resetToken.isExpired()) {
			model.addAttribute("error", "Token expirado, solicite novamente a recuperação");
		} else {
			model.addAttribute("email", email);
			model.addAttribute("token", resetToken.getToken());
			return "resetpassword";
		}

		return "login";
	}

	@RequestMapping(value = { "/admin", "/manager/botoes" }, method = RequestMethod.POST)
	public ModelAndView novoBotao(Button button, Model model,
			@RequestParam(value = "ideditar", required = false) long id) {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("ID DE MEU BOTAO" + id);
		if (id == -1 && button != null && button.getName() != null && button.getValue() != null
				&& button.getName() != "") {
			System.out.println("CHAMDAOAOAOAOAOOAOAOAOOAOAOOAOAOA");
			buttonService.saveButton(button);
		} else if (id > 0 && button != null && button.getName() != null && button.getValue() != null
				&& button.getName() != "") {
			button.setId(id);
			buttonService.updateButton(button);
		}

		Button b = new Button();
		modelAndView.addObject("mostrar", button);
		model.addAttribute("button", b);
		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "name");
		Page<Button> buts = this.buttonRepository.findAll(pageRequest);
		model.addAttribute("buttonsme", buts);
		modelAndView.setViewName("manager/botoes");
		return modelAndView;
	}

	@RequestMapping("/resets")
	@ResponseBody
	public String resetar(Model model, @RequestParam String token, @RequestParam String email,
			@RequestParam String password, @RequestParam String confirm, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("TA DENTRO");
		ModelAndView modelAndView = new ModelAndView();
		User us = userService.findUserByEmail(email);
		PasswordResetToken t = tokenService.findByToken(token);
		String result = "";
		System.out.println("PASSA POR AQUI------------------------------------------" + token);
		if (t.getUser().getId() != us.getId()) {
			System.out.println("ACESSO NEGADO");
			result = "ACESSO NEGADO";
		} else {
			if (!password.equals(confirm)) {
				result = "Senhas não conferem";
			} else if (password.length() < 5) {
				result = "A senha deve possuir ao menos 5 carcateres";
			} else {
				us.setPassword(bCryptPasswordEncoder.encode(password));
				userService.updateUser(us);
				result = "Senha alterada";
			}
		}
		return result;
	}

	@RequestMapping("/resets/delettoken")
	@ResponseBody
	public boolean resetar(Model model, @RequestParam String token, HttpServletRequest request,
			HttpServletResponse response) {
		PasswordResetToken t = tokenService.findByToken(token);
		System.out.println("FUI CHAMADO SIM");
		if (t != null) {
			System.out.println("ENCONTROU");
			tokenRepository.delete(t);
			System.out.println("ENCONTROU++");
			return true;
		}
		return false;
	}

	@RequestMapping(value = { "/user", "/user/edit/test" }, method = RequestMethod.POST)

	public String test(@RequestParam("arquivo") MultipartFile arquivo, Model model) throws IOException {
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
		modelAndView.setViewName("user/home");

		return "redirect:" + "/user/home";
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

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = { "/user", "/user/home" }, method = RequestMethod.POST)
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

		modelAndView.setViewName("user/home");
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

		if (isAdmin())
			return "redirect:" + "/manager/controle";
		if (isManager())
			return "redirect:" + "/manager";
		if (isUser())
			return "redirect:" + "/user";

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
	public String createNewUser(@Valid User user, BindingResult bindingResult, @RequestParam String hid,
			@RequestParam String confirm, HttpServletRequest request) throws ServletException {
		System.out.println("AaaAaAaaAaa" + hid);
		System.out.println("BbbbBbBBBbBbbBBb" + user.getDocument());
		user.setDocument(hid);
		String antes = user.getPassword();
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
			return "redirect:/registration/";

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
			return "redirect:/registration/";
		} else {
			user.setImage("rejoed05uyghymultqsv");
			user.generatePin();
			userService.saveUser(user);
			final String uri = "http://localhost:8030/email-send/{id}";
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", user.getId() + "");

			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uri, String.class, params);

			System.out.println(result);
			System.out.println("BELEZA");
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			// request.login(user.getEmail(), user.getPassword());

			modelAndView.setViewName("user/home");

		}
		System.out.println("MEU SENHA" + antes);
		request.login(user.getEmail(), antes);

		return "redirect:/user/home";
	}

	@RequestMapping(value = "/admin/registermanager", method = RequestMethod.GET)
	public ModelAndView regis(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.addObject("result", user.getName());

		PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.valueOf("ASC"), "name");

		Page<User> ur = this.userRepository.findManagers(pageRequest);
		model.addAttribute("managers", ur);

		modelAndView.setViewName("admin/registermanager");
		return modelAndView;
	}

	@RequestMapping(value = "/manager/buscar", method = RequestMethod.GET)
	public String searchuser(User u, Model model, @RequestParam("email") String email, Principal principal

	) {
		User us = userService.findUserByEmail(email);
		User u1 = userService.findUserByEmail(principal.getName());
		if (u1 == null)
			u1 = userService.findUserByDocument(principal.getName());
		aux = new ArrayList<Tag>();
		for (int i = 0; i < userService.findAll().size(); i++) {
			Tag tg = new Tag(i + 1, userService.findAll().get(i).getDocument());
			aux.add(tg);
		}
		long x = 1;
		System.out.println("ANTES DE INICIO ---------- AQUI");
		// UserBalance ub = userbalanceRepository.findOne(x);
		System.out.println("INICIO ---------- AQUI");
		if (us != null) {
			model.addAttribute("users", us);
			System.out.println("1111111111111111111111");
			UserBalance ub = userbalanceRepository.hasBalance(us.getId(), u1.getCompany().getId());
			System.out.println("2222222222222222222222222");
			if (ub != null && ub.getBalance() != null) {
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				String s = nf.format(ub.getBalance());
				model.addAttribute("valor", s);
			}
		} else if (us == null) {
			System.out.println("3333333333333333333333333333");
			System.out.println("TESTE" + u1.getCompany().getId());
			System.out.println("4444444444444444444444444");
			User use = userService.findUserByDocument(email);

			if (use != null) {
				System.out.println("ANTES");
				UserBalance ub = userbalanceRepository.hasBalance(use.getId(), u1.getCompany().getId());
				System.out.println("DEPOIS");
				model.addAttribute("users", use);
				List<Button> buttons = buttonService.findAll();
				Button button = new Button();
				buttons = buttonService.findAll();
				model.addAttribute("buttons", buttons);
				if (ub != null && ub.getBalance() != null) {
					NumberFormat nf = NumberFormat.getCurrencyInstance();
					String s = nf.format(ub.getBalance());
					model.addAttribute("valor", s);
				}

			} else
				model.addAttribute("usernotfound", "verdade");
		}

		return "manager/controle";
	}

	private static final String AJAX_HEADER_NAME = "X-Requested-With";
	private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

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
			Model model, RedirectAttributes redir, Principal princi) {
		User user = userService.getOne(iduser);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		User uaux = userService.findUserByEmail(username);
		if (uaux == null)
			uaux = userService.findUserByDocument(username);
		String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

		long millis = System.currentTimeMillis();

		TransactionCredit transactioncredit = new TransactionCredit();
		System.out.println("VEM ATE AQUI");
		UserBalance ub = userbalanceRepository.hasBalance(user.getId(), uaux.getCompany().getId());
		System.out.println("VEM ATE AQUI 1");
		if (debito != null) {
			System.out.println("O ERRO ESTA ABAIXO");
		}
		if (debito == null) {

			Button button = buttonService.getOne(idbutton);
			if (ub.getBalance() != null && button.getValue() != null
					&& (ub.getBalance().compareTo(button.getValue()) > 0
							|| ub.getBalance().compareTo(button.getValue()) == 0)) {
				transactioncredit.setButton(button);
				BigDecimal val = button.getValue();
				val = button.getValue().negate();
				transactioncredit.setValue(val);
				transactioncredit.setData(data);
				transactioncredit.setUser(user);
				transactioncredit.setMilis(millis);
				transactioncredit.setCompany(uaux.getCompany());
				transactioncredit.setOperator(uaux.getName());
				BigDecimal total = ub.getBalance().add(val);
				ub.setBalance(total);
				// userService.updateUser(user);
				userbalanceService.updateUserBalance(ub);
				transactioncreditService.saveTransaction(transactioncredit);
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				String s = nf.format(ub.getBalance());
				model.addAttribute("valor", s);
				model.addAttribute("status", "Consumo lanncado!");
				redir.addFlashAttribute("status2", "Consumo lanncado!");
			} else {
				model.addAttribute("status", "Saldo Insuficiente!");
			}

		} else {
			Button b = new Button();
			if ((ub.getBalance() != null && debito != null)
					&& (debito.compareTo(ub.getBalance()) < 0 || debito.compareTo(ub.getBalance()) == 0)) {
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
				transactioncredit.setMilis(millis);
				transactioncredit.setCompany(uaux.getCompany());
				transactioncredit.setData(data);
				transactioncredit.setOperator(uaux.getName());
				BigDecimal total = ub.getBalance().add(val);
				ub.setBalance(total);
				userService.updateUser(user);
				transactioncreditService.saveTransaction(transactioncredit);
				model.addAttribute("status", "Consumo lanncado!");
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				String s = nf.format(ub.getBalance());
				model.addAttribute("valor", s);
				redir.addFlashAttribute("status2", "Consumo lanncado!");
			} else {
				model.addAttribute("status", "Saldo Insuficiente!");
			}
			System.out.println("VALORRR" + debito);
			System.out.println("VALORRR" + description);
		}
		return searchuser(user, model, user.getDocument(), princi);

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
		String s = "";
		User uaux = userService.findUserByEmail(principal.getName());
		if (uaux == null)
			uaux = userService.findUserByDocument(principal.getName());

		System.out.println("STACK");
		UserBalance ub = userbalanceRepository.hasBalance(us.getId(), uaux.getCompany().getId());
		if (ub != null && ub.getBalance() != null) {
			balance = balance.add(ub.getBalance());
			ub.setBalance(balance);
			userbalanceService.updateUserBalance(ub);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			s = nf.format(ub.getBalance());

		}
		if (ub == null) {
			UserBalance bal = new UserBalance();
			bal.setBalance(balance);
			bal.setCompany(uaux.getCompany());
			bal.setUser(us);
			userbalanceService.saveUserBalance(bal);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			s = nf.format(bal.getBalance());
		}
		System.out.println("STACK222222222222");
		String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		long millis = System.currentTimeMillis();
		// ub.setBalance(balance);
		transactioncredit.setUser(us);
		Button button = new Button();

		// transactioncredit.setButton(button);
		transactioncredit.setMilis(millis);
		transactioncredit.setOperator(uaux.getName());
		transactioncredit.setCompany(uaux.getCompany());
		transactioncredit.setData(data);
		model.addAttribute("status", "Créditos Inseridos!");
		System.out.println("VEM ATE AQUI");
		// userbalanceService.updateUserBalance(ub);
		System.out.println("VEM ATE AQUI?");
		userService.updateUser(us);
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		// String s = nf.format(ub.getBalance());
		model.addAttribute("valor", s);
		// model.addAttribute("valor", s);
		transactioncreditService.saveTransaction(transactioncredit);

		model.addAttribute("users", us);
		List<Button> buttons = buttonService.findAll();
		model.addAttribute("buttons", buttons);
		System.out.println("PRINCIPALLLLLLLLLLL" + principal.getName());

		return searchuser(us, model, us.getDocument(), principal);
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
			@RequestParam("confirm") String confirm, Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("::::::::::::::::::::::::::::::::::::::::::::");
		if (user.getId() != -1) {
			modelAndView.addObject("successMessage", "editado");
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::111111111111111111111111111111");

			userService.updateManager(user, confirm);
			List<User> users = userService.findManager();
			model.addAttribute("managers", users);
			modelAndView.setViewName("admin/registermanager");
		} else {
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::2222222222222222222222222222");

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
				User uaux = userService.findUserByDocument(principal.getName());
				if (uaux == null)
					uaux = userService.findUserByEmail(principal.getName());
				user.setCompany(uaux.getCompany());
				user.setCompany(uaux.getCompany());

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
