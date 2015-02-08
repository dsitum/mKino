<?php
  class Filmovi
  {
    public static function DohvatiFilmove($aktualno)
    {
      require_once 'DB_connect.php';
      $upit = "SELECT idFilma, filmovi.naziv as naziv, opis, redatelj, glavneUloge, trajanje, godina, zanrovi.naziv as zanr, aktualno FROM filmovi JOIN zanrovi ON (filmovi.zanr = zanrovi.idZanra) WHERE aktualno = " . $aktualno;
      
      $filmovi = UpitUBazu($upit);
      return $filmovi;
    }
    public static function DohvatiRezervacije($korisnik)
    {
      require_once 'DB_connect.php';
      $upit = "SELECT idKorisnika FROM korisnici WHERE korisnickoIme = '$korisnik'";
      $idKorisnika = UpitUBazu($upit);
      $upit = "SELECT * FROM rezervacije LEFT JOIN projekcijefilmova ON rezervacije.projekcija = projekcijefilmova.idProjekcije WHERE projekcijefilmova.vrijemePocetka >= NOW() AND korisnik = " . $idKorisnika[0]['idKorisnika'] . " ORDER BY rezervacije.projekcija";
      $projekcije = UpitUBazu($upit);
      return $projekcije;
    }
    
    public static function DohvatiNekeFilmove($aktualno, $bezOvih)
    {
      require_once 'DB_connect.php';
      $upit = "SELECT idFilma, filmovi.naziv as naziv, opis, redatelj, glavneUloge, trajanje, godina, zanrovi.naziv as zanr, aktualno FROM filmovi JOIN zanrovi ON (filmovi.zanr = zanrovi.idZanra)";
      
      $filmovi = UpitUBazu($upit);
      
      $bezOvihFilmovaTmp = json_decode($bezOvih);
      $bezOvihFilmova = array();
      foreach($bezOvihFilmovaTmp as $bezOvog)
        $bezOvihFilmova[] = $bezOvog->{'idFilma'};
    
      $samoOviFilmovi = array();
      
      foreach($filmovi as $film)
      {
        $idFilma = $film["idFilma"];
        if (! in_array($idFilma, $bezOvihFilmova) && $film["aktualno"] == 1)
        {
          $samoOviFilmovi[] = $film;
        }
        
        if (in_array($idFilma, $bezOvihFilmova) && $film["aktualno"] == 0)
        {
          $samoOviFilmovi[] = $film;
        }
      }
      
      return $samoOviFilmovi;
    }
    
    public static function DohvatiSveProjekcije($film, $multipleks)
    {
      require_once 'DB_connect.php';
      $upit = "SELECT idProjekcije, vrijemePocetka, cijena, brojDvorane FROM projekcijefilmova JOIN dvorane ON (projekcijefilmova.dvorana = dvorane.idDvorane) JOIN multipleksi ON (dvorane.multipleks = multipleksi.idMultipleksa) WHERE projekcijefilmova.film = $film AND dvorane.multipleks = $multipleks AND DATE(vrijemePocetka) >= DATE(DATE_ADD(NOW(), INTERVAL 1 HOUR))";
      $projekcije = UpitUBazu($upit);
      return $projekcije;
    }
    
    public static function DohvatiProjekcijeNaDan($film, $multipleks, $datum)
    {
      require_once 'DB_connect.php';
      $upit = "SELECT idProjekcije, vrijemePocetka, cijena, brojDvorane FROM projekcijefilmova JOIN dvorane ON (projekcijefilmova.dvorana = dvorane.idDvorane) JOIN multipleksi ON (dvorane.multipleks = multipleksi.idMultipleksa) WHERE projekcijefilmova.film = $film AND dvorane.multipleks = $multipleks AND DATE(vrijemePocetka) = '$datum'";
      $projekcije = UpitUBazu($upit);
      return $projekcije;
    }
                public static function DohvatiProjekcijeMultipleks($multipleks, $bezOvih)
                {
                    require_once 'DB_connect.php';
                    $danasnjiDatum = date('Y-m-d H:i:s', time());
                    $danasnjiDatum = date('Y-m-d', strtotime($danasnjiDatum . '+ 30 minute'));
                    $iduca2Tjedna = date('Y-m-d', strtotime($danasnjiDatum . '+ 14 day'));
                    $upit = "SELECT p.idProjekcije, p.film, p.vrijemePocetka, p.cijena, d.brojDvorane, d.multipleks FROM projekcijefilmova AS p LEFT JOIN dvorane AS d ON p.dvorana = d.idDvorane WHERE d.multipleks = $multipleks AND p.vrijemePocetka >= DATE_ADD(NOW(), INTERVAL 1 HOUR) ORDER BY p.film ASC ,  `p`.`vrijemePocetka` ASC";
                    $projekcije = UpitUBazu($upit);
                    
                    try{
                        $bezOvihProjekcijaTmp = json_decode($bezOvih);
      $bezOvihProjekcija = array();
                        if(!empty($bezOvihProjekcijaTmp))
                        {
                                foreach($bezOvihProjekcijaTmp as $bezOvog)
        $bezOvihProjekcija[] = $bezOvog->{'idProjekcije'};
                                        }
                    }
                    catch( Exception $e){
                        
                    }
      $samoOveProjekcije = array();
      
      foreach($projekcije as $projekcija)
      {
                             
                                if(($projekcija['vrijemePocetka'] >= $danasnjiDatum) && ($projekcija['vrijemePocetka'] <= $iduca2Tjedna))
                                {
                                        $uvjet = true;
                                }
                                else 
                                {
                                        $uvjet=false;
                                }
                                
                                
                              
        $idProjekcije = $projekcija["idProjekcije"];
                                if (! in_array($idProjekcije, $bezOvihProjekcija) && ($uvjet == true))
        {
                                       
          $samoOveProjekcije[] = $projekcija;
        }
                                if ( in_array($idProjekcije, $bezOvihProjekcija)&& ($uvjet == false))
        {
          $samoOveProjekcije[] = $projekcija;
        }
      }      
      return $samoOveProjekcije;              
                }
                public static function DohvatiSjedalaProjekcija($projekcija)
                {
                    require_once 'DB_connect.php';
                    $upit = "SELECT brojSjedala FROM rezervacije WHERE projekcija =".$projekcija." order by brojSjedala";
                    
                    $projekcije = UpitUBazu($upit);
                    return $projekcije;
                }
                public static function Rezerviraj($projekcija, $korisnik, $sjedalaJSON)
                {
                
                    $zapis = array();
                    require_once 'DB_connect.php';
                    $upit = "SELECT idKorisnika FROM korisnici WHERE korisnickoIme = '".$korisnik."'";
                    
                    $idKorisnika = UpitUBazu($upit);                   
                 
                    $sjedalaTmp = json_decode($sjedalaJSON);                  
                    $sjedala = array();
                
                    $kodRezervacije =  $idKorisnika[0]['idKorisnika'] ."-".$projekcija;
               
                     $zauzetoMjesto = 0;
                     for($i = 0; $i < count(sjedalaTmp); $i++)
                     {
                        $upit = "SELECT * FROM rezervacije WHERE projekcija =".$projekcija." AND brojSjedala=".(string)$sjedalaTmp[$i]->{'sjedalo'}."";
                         $rezervacije = UpitUBazu($upit);
                       if($rezervacije[0][povratnaInformacijaId] !== 1) $zauzetoMjesto=1;                     
                       
                     }                    
                        
                     if($zauzetoMjesto === 0)
                     {
                         for($i = 0; $i < sizeof($sjedalaTmp,1); $i++)
                         {
                              $upit = "INSERT INTO rezervacije VALUES (DEFAULT,".$projekcija.", ".(string)$idKorisnika[0]['idKorisnika'].", ".(string)$sjedalaTmp[$i]->{'sjedalo'}.", '".$kodRezervacije."')";
                              $uspjesnost = Transakcija($upit);
                                       
                          }
                           if($uspjesnost === -6)
                           {
                                  $zapis[0] = array("povratnaInformacijaId" => -6, "povratnaInformacijaTekst" => utf8_encode("Rezervacija nije uspjela!"));
                                                       
                            }
                            else
                            {
                              $zapis[0] = array("povratnaInformacijaId" => $kodRezervacije , "povratnaInformacijaTekst" => utf8_encode("Rezervacija uspjesna!"));
                             }
                       }
                       else
                       {
                              $zapis[0] = array("povratnaInformacijaId" => -7, "povratnaInformacijaTekst" => utf8_encode("Ulaznice u međuvremenu rezervirane!"));
                        } 
                        return $zapis;
                }
    
            
                public static function ObrisiMojuRezervaciju($korisnik, $idProjekcije)
                {
                    require_once 'DB_connect.php';
                    $upit = "SELECT idKorisnika FROM korisnici WHERE korisnickoIme = '$korisnik'";
                    $idKorisnikaTmp = UpitUBazu($upit);
					$idKorisnika = $idKorisnikaTmp[0]['idKorisnika'];
                    
                    $upit = "DELETE FROM rezervacije WHERE kod='$idKorisnika-$idProjekcije'";
                    $uspjelo = BrisanjeIzBaze($upit);
                    return $uspjelo;
                }
                            
}
?>