<?php
    //klasa za izvršavanje upita nad bazom
    define("HOST", "localhost");
    define("USER", "root");
    define("PASS", "123456");
    define("BAZA", "airbaza");

    class Upit {
        public $conn;

        function __construct() {
            $this->conn = new mysqli(HOST, USER, PASS, BAZA);
            if ($this->conn->connect_errno) {
                die('Pogreška pri spajanju na bazu podataka. Jesu li podaci ispravno uneseni? MySQL kaže: <br>'
                    . $this->conn->connect_error);
            } 
            $this->conn->set_charset("utf8");
        }

        function Izvrsi($upit) {
            $rezultat = $this->conn->query($upit);

            if(! $rezultat) {
                die("Problem pri uonsu u bazu. MySQL kaže: <br>" . $this->conn->error);
            } else {
                return $rezultat;
            }
        }

        function __destruct() {
            $this->conn->close();
        }
   
    }
    
//******************************************************************************************************************//
    //Ova funkcija služi za brže izvršavanje sql upita
    function Upit($naredba) {
    /*$upit = new Upit();
    $upit->Izvrsi("SET CHARACTER SET utf8");
    unset($upit);*/
        $upit = new Upit();
        $rezultat = $upit->Izvrsi($naredba);
        unset($upit);
        return $rezultat;
    }
         //transakcije
    function begin()
    {
        Upit("BEGIN");
    }
    function commit()
    {
        Upit("COMMIT");
    }
    function rollback()
    {
       Upit("ROLLBACK");
    }
    //funkcija za izvršavanje transakcije
    function Transakcija($upit)
    {
            $rezultat = Upit($upit); 
     
            if ($rezultat > 0)
            {
              // $rezultat->fetch_assoc();      
                    return $rezultat;

            } else
            {             
              //print_r($rezultat);
                    $zapis = -6;
                    return $zapis;
            }
    }
	
	function BrisanjeIzBaze($naredba)
	{
		$upit = new Upit();
		$upit->Izvrsi($naredba);
		
		$zapis = array();
		if ($upit->conn->affected_rows > 0)
		{
			$zapis[0] = array("povratnaInformacijaId" => 0, "povratnaInformacijaTekst" => utf8_encode("Uspješno obrisano"));
		} else
		{
			$zapis[0] = array("povratnaInformacijaId" => -8, "povratnaInformacijaTekst" => utf8_encode("Ništa nije obrisano"));
		}
		
		return $zapis;
	}
        
  function UpitUBazu($upit)
  {
    $rezultat = Upit($upit);
    $zapisi = array();
    if ($rezultat->num_rows > 0)
    {
      while ($zapis = $rezultat->fetch_assoc())
      {
        $zapisi[] = $zapis;
      }
      
      return $zapisi;
    } else
    {
      $zapis = array();
      $zapis[0] = array("povratnaInformacijaId" => 1, "povratnaInformacijaTekst" => utf8_encode("Nema zapisa"));
      return $zapis;
    }
  }
?>