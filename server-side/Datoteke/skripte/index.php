<?php
  // filmovi
  if ($_GET['tip'] == 'filmovi')
  {
    require_once 'filmovi.php';
    $filmovi = Filmovi::DohvatiFilmove($_GET['aktualno']);
    echo json_encode($filmovi);
  } 
    else if($_GET['tip'] == 'mr')
  {
    
    require_once 'filmovi.php';    
    $korisnik = $_GET['korisnik'];
    $uspjesno = Filmovi::DohvatiRezervacije($korisnik);
    echo json_encode($uspjesno);
  }
  else if ($_GET['tip'] == 'nekifilmovi')
  {
    require_once 'filmovi.php';
    $bezOvihFilmova = $_POST['bezOvihFilmova'];
    $filmovi = Filmovi::DohvatiNekeFilmove($_GET['aktualno'], $bezOvihFilmova);
    echo json_encode($filmovi);
  }
  
  // sve projekcije 
  else if ($_GET['tip'] == 'projekcije')
  {
    require_once 'filmovi.php';
    $projekcije = Filmovi::DohvatiSveProjekcije($_GET['film'], $_GET['multipleks']);
    echo json_encode($projekcije);
  }
  
  // projekcije na danaĹˇnji dan
  else if ($_GET['tip'] == 'projekcijeDanas')
  {
    require_once 'filmovi.php';
    $datum = date('Y-m-d');
    $projekcije = Filmovi::DohvatiProjekcijeNaDan($_GET['film'], $_GET['multipleks'], $datum);
    echo json_encode($projekcije);
  }
  
        // sve projekcije za multipleks
  else if ($_GET['tip'] == 'projekcijeMultipleks')
  {
    require_once 'filmovi.php';
    $bezOvihProjekcija = $_POST['bezOvihProjekcija'];
    $projekcije = Filmovi::DohvatiProjekcijeMultipleks($_GET['multipleks'], $bezOvihProjekcija);
    echo json_encode($projekcije);
  }
  // sjedala za projekciju
  else if ($_GET['tip'] == 'sjedalaProjekcija')
  {
    require_once 'filmovi.php';    
    $sjedala = Filmovi::DohvatiSjedalaProjekcija($_GET['projekcija']);
    echo json_encode($sjedala);
  }
  //rezervacija ulaznica
  else if($_GET['tip'] == 'rezervacija')
  {
    require_once 'filmovi.php';    
    $sjedala = $_POST['sjedala'];
    $korisnik =  $_POST['korisnik'];
    $projekcija = $_POST['projekcija'];
    
    $uspjesno = Filmovi::Rezerviraj($projekcija,$korisnik, $sjedala);
    echo json_encode($uspjesno);
  }
  //moje rezervacije

   //moje rezervacije
  else if($_GET['tip'] == 'obrisirezervaciju')
  {
    require_once 'filmovi.php'; 
	$korisnickoIme = $_POST['korisnickoime'];
    $projekcija = $_POST['projekcija'];
    
    $uspjesno = Filmovi::ObrisiMojuRezervaciju($korisnickoIme, $projekcija);
    echo json_encode($uspjesno);
  }
  // projekcije na odreÄ‘eni dan
  else if ($_GET['tip'] == 'projekcijeNaDan')
  {
    require_once 'filmovi.php';
    $godina = $_GET['godina'];
    $mjesec = sprintf('%02d', $_GET['mjesec']);
    $dan = sprintf('%02d', $_GET['dan']);
    $datum = "$godina-$mjesec-$dan";
    $projekcije = Filmovi::DohvatiProjekcijeNaDan($_GET['film'], $_GET['multipleks'], $datum);
    echo json_encode($projekcije);
  }
  
  // prijava korisnika
  else if ($_GET['tip'] == 'prijava')
  {
    require_once 'korisnici.php';
    $korisnik = Korisnici::PrijaviSe($_POST['korisnickoIme'], $_POST['lozinka']);
    echo json_encode($korisnik);
  }
  
  // registracija korisnika
  else if ($_GET['tip'] == 'registracija')
  {
    require_once 'korisnici.php';
    $podaci = array();
    $podaci['korisnickoIme'] = $_POST['korisnickoIme'];
    $podaci['salt'] = hash('sha512', time());
    $podaci['lozinka'] = hash('sha512', $_POST['lozinka'] . $podaci['salt']);
    $podaci['ime'] = $_POST['ime'];
    $podaci['prezime'] = $_POST['prezime'];
    $podaci['email'] = $_POST['email'];
    $podaci['telefon'] = $_POST['telefon'];
    $povratnaInformacija = Korisnici::RegistrirajSe($podaci);
    echo json_encode($povratnaInformacija);
  }
  
  // dohvat multipleksa
  else if ($_GET['tip'] == 'multipleksi')
  {
    require_once 'DB_connect.php';
    $upit = "SELECT * FROM multipleksi";
    $multipleksi = UpitUBazu($upit);
    echo json_encode($multipleksi);
  }
  
  // dohvat velikih slika
  else if ($_GET['tip'] == 'slike')
  {
    switch ($_GET['id'])
    {
      case "2": header("Location: http://i.imgur.com/41duaTf.jpg"); break;
      case "3": header("Location: http://i.imgur.com/Lz9I5IV.jpg"); break;
      case "4": header("Location: http://i.imgur.com/5QHm0os.jpg"); break;
      case "5": header("Location: http://i.imgur.com/W4ES8Cy.jpg"); break;
      case "6": header("Location: http://i.imgur.com/ikGcaAB.jpg"); break;
      case "7": header("Location: http://i.imgur.com/MzjVnsg.jpg"); break;
      case "8": header("Location: http://i.imgur.com/M7MnWkj.jpg"); break;
      case "9": header("Location: http://i.imgur.com/A9DfZlk.jpg"); break;
      case "10": header("Location: http://i.imgur.com/VKTJTOV.jpg"); break;
      
      case "11": header("Location: http://i.imgur.com/b4xOyuV.jpg"); break;
      case "12": header("Location: http://i.imgur.com/LCJFEUG.jpg"); break;
      case "13": header("Location: http://i.imgur.com/JJOt68E.jpg"); break;
      case "14": header("Location: http://i.imgur.com/P2FxpMW.jpg"); break;
      case "15": header("Location: http://i.imgur.com/TU0Z9iw.jpg"); break;
      case "16": header("Location: http://i.imgur.com/yd5Ogel.jpg"); break;
      case "17": header("Location: http://i.imgur.com/TYWQOH8.jpg"); break;
      case "18": header("Location: http://i.imgur.com/RznPMMI.jpg"); break;
      case "19": header("Location: http://i.imgur.com/X7PtQu4.jpg"); break;
      case "20": header("Location: http://i.imgur.com/cmODA5w.jpg"); break;
      
      case "21": header("Location: http://i.imgur.com/alUguKF.jpg"); break;
      case "22": header("Location: http://i.imgur.com/nGHZK2L.jpg"); break;
      case "23": header("Location: http://i.imgur.com/4f7JLUL.jpg"); break;
      case "24": header("Location: http://i.imgur.com/K53NSsu.jpg"); break;
      case "25": header("Location: http://i.imgur.com/yjoEYrl.jpg"); break;
      case "26": header("Location: http://i.imgur.com/ftgC6GD.jpg"); break;
      case "27": header("Location: http://i.imgur.com/OlSyMT5.jpg"); break;
      case "28": header("Location: http://i.imgur.com/W2PgXVM.jpg"); break;
      case "29": header("Location: http://i.imgur.com/R6Nfg64.jpg"); break;
      case "30": header("Location: http://i.imgur.com/xu5dxMn.jpg"); break;
      
      case "31": header("Location: http://i.imgur.com/OT1S7Ab.jpg"); break;
      case "32": header("Location: http://i.imgur.com/LBVVjKB.jpg"); break;
      case "33": header("Location: http://i.imgur.com/xlyP7ey.jpg"); break;
      case "34": header("Location: http://i.imgur.com/ycV5Jbm.jpg"); break;
      case "35": header("Location: http://i.imgur.com/SFvYvfO.jpg"); break;
      case "36": header("Location: http://i.imgur.com/8ksN3w7.jpg"); break;
      case "37": header("Location: http://i.imgur.com/RISNSTL.jpg"); break;
      case "38": header("Location: http://i.imgur.com/GVMkesY.jpg"); break;
      case "39": header("Location: http://i.imgur.com/J1PSoao.jpg"); break;
      case "40": header("Location: http://i.imgur.com/ma4wI5O.jpg"); break;
      
      case "41": header("Location: http://i.imgur.com/bvyvVZH.jpg"); break;
      case "42": header("Location: http://i.imgur.com/H0lry9h.jpg"); break;
      case "43": header("Location: http://i.imgur.com/az7PGpb.jpg"); break;
      case "44": header("Location: http://i.imgur.com/vPPo6id.jpg"); break;
      case "45": header("Location: http://i.imgur.com/ikNySIx.jpg"); break;
      case "46": header("Location: http://i.imgur.com/4NEJUfJ.jpg"); break;
      case "47": header("Location: http://i.imgur.com/tYhjKwx.jpg"); break;
      case "48": header("Location: http://i.imgur.com/Xnl2Jfm.jpg"); break;
      case "49": header("Location: http://i.imgur.com/lmsD41e.jpg"); break;
    }
  }
  
  
  // dohvat malih slika
  else if ($_GET['tip'] == 'slikemale')
  {
	switch ($_GET['id'])
	{
	  case "2": header("Location: http://i.imgur.com/fhuwmjN.jpg"); break;
      case "3": header("Location: http://i.imgur.com/shncnOp.jpg"); break;
      case "4": header("Location: http://i.imgur.com/l1ZkxI2.jpg"); break;
      case "5": header("Location: http://i.imgur.com/YQbfygu.jpg"); break;
      case "6": header("Location: http://i.imgur.com/oPdiKUZ.jpg"); break;
      case "7": header("Location: http://i.imgur.com/NBsc8a0.jpg"); break;
      case "8": header("Location: http://i.imgur.com/xI5ayDs.jpg"); break;
      case "9": header("Location: http://i.imgur.com/zNTEjWw.jpg"); break;
      case "10": header("Location: http://i.imgur.com/Gpb6t9t.jpg"); break;
      
      case "11": header("Location: http://i.imgur.com/6CdX34K.jpg"); break;
      case "12": header("Location: http://i.imgur.com/CD5y8OS.jpg"); break;
      case "13": header("Location: http://i.imgur.com/9jJXkJs.jpg"); break;
      case "14": header("Location: http://i.imgur.com/NJlzEih.jpg"); break;
      case "15": header("Location: http://i.imgur.com/tfCJrV6.jpg"); break;
      case "16": header("Location: http://i.imgur.com/aQqIrbx.jpg"); break;
      case "17": header("Location: http://i.imgur.com/rLuAtwh.jpg"); break;
      case "18": header("Location: http://i.imgur.com/xVkne5N.jpg"); break;
      case "19": header("Location: http://i.imgur.com/BTxPbMM.jpg"); break;
      case "20": header("Location: http://i.imgur.com/mtWD9es.jpg"); break;
      
      case "21": header("Location: http://i.imgur.com/uDhILCV.jpg"); break;
      case "22": header("Location: http://i.imgur.com/SNU5hAY.jpg"); break;
      case "23": header("Location: http://i.imgur.com/Y3CwUNK.jpg"); break;
      case "24": header("Location: http://i.imgur.com/Y0l9Q4z.jpg"); break;
      case "25": header("Location: http://i.imgur.com/ssnK6Vm.jpg"); break;
      case "26": header("Location: http://i.imgur.com/v1h7CAi.jpg"); break;
      case "27": header("Location: http://i.imgur.com/zjbVdfr.jpg"); break;
      case "28": header("Location: http://i.imgur.com/fQfICzc.jpg"); break;
      case "29": header("Location: http://i.imgur.com/DNfo0aF.jpg"); break;
      case "30": header("Location: http://i.imgur.com/ycJ8KLs.jpg"); break;
      
      case "31": header("Location: http://i.imgur.com/z8Hd68m.jpg"); break;
      case "32": header("Location: http://i.imgur.com/Ewhy0AZ.jpg"); break;
      case "33": header("Location: http://i.imgur.com/XXxwvIC.jpg"); break;
      case "34": header("Location: http://i.imgur.com/Dpp0M94.jpg"); break;
      case "35": header("Location: http://i.imgur.com/WVtj1mL.jpg"); break;
      case "36": header("Location: http://i.imgur.com/CAiV3FU.jpg"); break;
      case "37": header("Location: http://i.imgur.com/xuuSCoM.jpg"); break;
      case "38": header("Location: http://i.imgur.com/dVUNbps.jpg"); break;
      case "39": header("Location: http://i.imgur.com/DETgBHh.jpg"); break;
      case "40": header("Location: http://i.imgur.com/1giMHc5.jpg"); break;
      
      case "41": header("Location: http://i.imgur.com/vHGma35.jpg"); break;
      case "42": header("Location: http://i.imgur.com/5RqA71y.jpg"); break;
      case "43": header("Location: http://i.imgur.com/pje2KYS.jpg"); break;
      case "44": header("Location: http://i.imgur.com/ExWIfhd.jpg"); break;
      case "45": header("Location: http://i.imgur.com/fZFGUJ6.jpg"); break;
      case "46": header("Location: http://i.imgur.com/NCy0e8k.jpg"); break;
      case "47": header("Location: http://i.imgur.com/6xXmYyG.jpg"); break;
      case "48": header("Location: http://i.imgur.com/qCOpBtG.jpg"); break;
      case "49": header("Location: http://i.imgur.com/ArseZX8.jpg"); break;
	}
  }
  
  // pogreĹˇka
  else
  {
    echo "Nisu uneseni parametri";
  }
?> 