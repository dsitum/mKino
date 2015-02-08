<?php
	class Korisnici
	{
		public static function PrijaviSe($korisnickoIme, $lozinka)
		{
			require_once 'DB_connect.php';
			$upit = "SELECT * FROM korisnici WHERE korisnickoIme = '$korisnickoIme'";
			$korisnik = UpitUBazu($upit);
			if (isset($korisnik[0]['povratnaInformacijaId']))
			{
				return array(array('povratnaInformacijaId' => 2, 'povratnaInformacijaTekst' => utf8_encode('Nepostojeæi korisnik')));
			} else
			{
				$lozinkaHash = hash('sha512', $lozinka . $korisnik[0]['salt']);
			}
			
			$upit = "SELECT idKorisnika, korisnickoIme, ime, prezime, email, telefon FROM korisnici WHERE korisnickoIme = '$korisnickoIme' AND lozinka = '$lozinkaHash'";
			$korisnik = UpitUBazu($upit);
			if (isset($korisnik[0]['povratnaInformacijaId']))
			{
				return array(array('povratnaInformacijaId' => 3, 'povratnaInformacijaTekst' => utf8_encode('Pogrešna lozinka')));
			}
			
			return $korisnik;
		}
		
		public static function RegistrirajSe($podaci)
		{
			require_once 'DB_connect.php';
			// provjeravamo postoji li korisnièko ime veæ u bazi
			$upit = "SELECT * FROM korisnici WHERE korisnickoIme = '" . $podaci['korisnickoIme'] . "'";
			$korisnik = UpitUBazu($upit);
			if (isset($korisnik[0]['korisnickoIme']))
			{
				return array(array('povratnaInformacijaId' => 4, 'povratnaInformacijaTekst' => utf8_encode('Korisnik veæ postoji'))); 
			}
			
			// provjeravamo postoji li email veæ u bazi
			$upit = "SELECT * FROM korisnici WHERE email = '" . $podaci['email'] . "'";
			$korisnik = UpitUBazu($upit);
			if (isset($korisnik[0]['email']))
			{
				return array(array('povratnaInformacijaId' => 5, 'povratnaInformacijaTekst' => utf8_encode('Email adresa veæ postoji'))); 
			}
			
			// provjeravamo postoji li telefon veæ u bazi
			$upit = "SELECT * FROM korisnici WHERE telefon = '" . $podaci['telefon'] . "'";
			$korisnik = UpitUBazu($upit);
			if (isset($korisnik[0]['telefon']))
			{
				return array(array('povratnaInformacijaId' => 6, 'povratnaInformacijaTekst' => utf8_encode('Taj telefonski broj veæ postoji'))); 
			}
			
			// ako se nije dogodio nikakav konflikt, dodajemo korisnika u bazu
			$upit = "INSERT INTO korisnici VALUES (DEFAULT, '".$podaci['korisnickoIme']."', '".$podaci['lozinka']."', '".$podaci['salt']."', '".$podaci['ime']."', '".$podaci['prezime']."', '".$podaci['email']."', '".$podaci['telefon']."')";
			$uspjeh = Upit($upit);
			
			if ($uspjeh)
			{
				// pronalazimo id trenutno unesenog korisnika
				$upit2 = "SELECT idKorisnika from korisnici WHERE korisnickoIme = '" . $podaci['korisnickoIme'] . "'";
				$id = UpitUBazu($upit2);
				
				// unosimo jednu praznu rezervaciju
				$idk = $id[0]['idKorisnika'];
				$upit3 = "INSERT INTO rezervacije (projekcija, korisnik, brojSjedala, kod) VALUES ($idk, $idk, $idk, '$idk-0')";
				Upit($upit3);
				return array(array('povratnaInformacijaId' => 0, 'povratnaInformacijaTekst' => utf8_encode('Korisnik uspješno dodan'))); 
			} else
			{
				return array(array('povratnaInformacijaId' => 7, 'povratnaInformacijaTekst' => utf8_encode('Korisnika nije bilo moguæe dodati u bazu. Razlog nepoznat'))); 
			}
		}
	}
?>