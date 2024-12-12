package edu.agh.susgame.back.domain.models.quiz

val QuizQuestions = listOf(
    QuizQuestion(
        question = "Skąd najlepiej pobierać aplikacje na telefon?",
        answers = listOf(
            "Z dowolnej strony internetowej",
            "Tylko od znajomych",
            "Z Google Play lub Apple App Store",
            "Z pierwszego znalezionego linku w wyszukiwarce"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co oznacza 'dobre hasło'?",
        answers = listOf(
            "Hasło składa się z co najmniej 8 znaków, w tym liter, cyfr i znaków specjalnych",
            "Hasło to '123456'",
            "Hasło to twoje imię i rok urodzenia",
            "Hasło to jedno słowo, np. 'hasło'"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Dlaczego nie powinno się klikać w podejrzane linki od znajomych?",
        answers = mutableListOf(
            "Bo mogą nie działać",
            "Bo są za długie",
            "Bo zajmują dużo miejsca w telefonie",
            "Bo mogą prowadzić do niebezpiecznych stron i wirusów",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Co należy zrobić przed zainstalowaniem aplikacji?",
        answers = listOf(
            "Od razu ją pobrać i uruchomić",
            "Zapisać ją na pendrivie",
            "Nie zwracać uwagi na nic, jeśli wygląda ciekawie",
            "Przeczytać opinie, sprawdzić oceny i uprawnienia aplikacji",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Dlaczego warto zainstalować program antywirusowy?",
        answers = listOf(
            "Żeby komputer działał szybciej",
            "Bo tak radzą znajomi",
            "Żeby łatwiej było znaleźć pliki na komputerze",
            "Żeby ochronić się przed wirusami i szkodliwym oprogramowaniem",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Jakie informacje należy zachować dla siebie podczas korzystania z komunikatora?",
        answers = listOf(
            "Swoje imię i nazwisko",
            "Ulubione filmy",
            "Adres zamieszkania, numer telefonu i inne poufne dane",
            "Plany na wakacje"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co tracisz, wysyłając zdjęcie do internetu?",
        answers = listOf("Kolor zdjęcia", "Pamięć o zdjęciu", "Kontrolę nad zdjęciem", "Kopię zapasową zdjęcia"),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Jak powinniśmy się zachowywać podczas rozmów w komunikatorach?",
        answers = listOf(
            "Obrażać innych",
            "Szanować innych i nie nękać nikogo",
            "Pisać tylko wielkimi literami",
            "Nie odpisywać na wiadomości"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co zrobić, jeśli chcesz grać w gry komputerowe?",
        answers = listOf(
            "Pobierać je z zaufanych źródeł",
            "Pytać znajomych o linki do gier",
            "Ściągać je z przypadkowych stron",
            "Grać bez instalowania czegokolwiek"
        ),
        correctAnswer = 0
    ),

    QuizQuestion(
        question = "Jak przeciwdziałać uzależnieniu od liczby polubień?",
        answers = listOf(
            "Zawsze klikać 'lubię to' na każdej fotografii",
            "Świadomie decydować, czy naprawdę podoba się zdjęcie",
            "Sprawdzać, ile lajków zebrało zdjęcie znajomych",
            "Polubić wszystkie zdjęcia o dużej liczbie lajków"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co oznacza skrót FOMO?",
        answers = listOf(
            "Focus on Mobile Online",
            "Fear of Missing Out",
            "Feel on My Own",
            "Fun of Media Online"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Jak możemy walczyć z FOMO?",
        answers = listOf(
            "Korzystać z internetu dłużej niż zwykle",
            "Ignorować znajomych w realu i skupić się na sieci",
            "Cieszyć się chwilą i zadbać o relacje w realu",
            "Udostępniać więcej zdjęć w mediach społecznościowych"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Dlaczego wielozadaniowość zmniejsza produktywność?",
        answers = listOf(
            "Ludzki mózg lepiej pracuje pod presją",
            "Wielozadaniowość zwiększa koncentrację",
            "Wielozadaniowość poprawia pamięć krótkotrwałą",
            "Skupienie na jednym zadaniu jest bardziej efektywne",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Co oznacza metoda 20 minut?",
        answers = listOf(
            "Praca nad zadaniem przez 20 minut bez przerwy po których następuje 5 minut przerwy",
            "Sprawdzanie powiadomień co 20 minut",
            "Zmiana zadania co 20 minut",
            "Używanie telefonu przez 20 minut dziennie"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Co powinniśmy zrobić, aby zadbać o zdrowy sen?",
        answers = listOf(
            "Używać telefonu przed snem",
            "Spać mniej niż 5 godzin",
            "Unikać niebieskiego światła na godzinę przed snem",
            "Grać w gry komputerowe przed snem"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Jakie objawy mogą wskazywać na FOMO?",
        answers = listOf(
            "Pełne zadowolenie z życia offline",
            "Brak jakichkolwiek emocji związanych z internetem",
            "Poprawa wyników w nauce",
            "Ciągła potrzeba korzystania z internetu",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Dlaczego powinniśmy planować czas poza siecią?",
        answers = listOf(
            "Aby uniknąć nudy i rozwijać hobby",
            "Aby grać więcej w gry online",
            "Aby zaniedbać obowiązki szkolne",
            "Aby spędzać więcej czasu na mediach społecznościowych"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Co może spowodować nadużywanie nowych technologii?",
        answers = listOf(
            "Poprawę zdrowia",
            "Zwiększenie liczby przyjaciół",
            "Obniżenie poczucia własnej wartości",
            "Poprawę wyników w nauce"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co powinniśmy zrobić, aby uniknąć wad postawy?",
        answers = listOf(
            "Pochylać głowę korzystając z telefonu",
            "Trzymać telefon na wysokości oczu",
            "Siedzieć przez długie godziny przed komputerem",
            "Nie zwracać uwagi na swoją postawę"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Dlaczego warto ograniczać przekąski podczas oglądania filmów?",
        answers = listOf(
            "Aby jeść więcej zdrowych rzeczy",
            "Aby zmniejszyć problemy zdrowotne",
            "Aby koncentrować się na ekranie",
            "Aby zyskać więcej energii"
        ),
        correctAnswer = 1
    ),


    QuizQuestion(
        question = "Co oznacza termin 'hejt'?",
        answers = listOf(
            "Dobre komentarze w sieci",
            "Obrażanie i ośmieszanie w internecie",
            "Udostępnianie zdjęć",
            "Chwalenie się w internecie"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co należy zrobić, zanim opublikujesz zdjęcie z innymi osobami?",
        answers = listOf(
            "Zapisać je na dysku",
            "Zapytaj ich o zgodę",
            "Udostępnić je w tajemnicy",
            "Nic, to tylko zdjęcie"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Dlaczego nie powinno się publikować zdjęć z widocznym adresem zamieszkania?",
        answers = listOf(
            "Bo adres może się zmienić",
            "To jest nielegalne",
            "Może to prowadzić do kradzieży",
            "Nikt nie zobaczy zdjęcia"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co zrobić, jeśli ktoś grozi ci w internecie?",
        answers = listOf("Odpowiedzieć groźbami", "Zignorować", "Poinformować dorosłego", "Zmienić profil"),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Dlaczego warto zabezpieczyć swoje urządzenia hasłem?",
        answers = listOf(
            "Bo to jest modne",
            "Aby szybciej się logować",
            "Żeby wyglądać profesjonalnie",
            "Aby chronić dane",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Czym jest 'wizerunek' w internecie?",
        answers = listOf(
            "Zdjęcia znajomych",
            "Tylko twój ubiór",
            "To, jak postrzegają cię inni",
            "Lista twoich znajomych"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co należy zrobić, jeśli jakaś interakcja w internecie budzi twój lęk lub strach?",
        answers = listOf("Poinformować dorosłego", "Usunąć konto", "Napisać post", "Nic nie robić"),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Dlaczego nie warto udostępniać zbyt wielu danych osobowych w internecie?",
        answers = listOf(
            "Bo zajmują miejsce",
            "To może narazić cię na niebezpieczeństwo",
            "Ludzie mogą się nudzić",
            "To jest nielegalne"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co zrobić, jeśli nie jesteś pewien, czy zdjęcie może być opublikowane w internecie?",
        answers = listOf(
            "Zapytaj rodzica lub nauczyciela",
            "Opublikuj od razu",
            "Usuń zdjęcie",
            "Zignoruj problem"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Dlaczego warto ustawić prywatność swoich zdjęć w sieci?",
        answers = listOf(
            "Aby ograniczyć dostęp tylko dla znajomych",
            "Bo to jest darmowe",
            "By móc je szybko usunąć",
            "By więcej osób je zobaczyło"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Co może się stać, jeśli klikniesz w link od nieznajomej osoby?",
        answers = listOf(
            "Nic złego",
            "Komputer się wyłączy",
            "Link się nie otworzy",
            "Twoje dane mogą zostać ukradzione"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Co oznacza termin 'cyberprzemoc'?",
        answers = listOf(
            "Pomoc online",
            "Agresja i prześladowanie w internecie",
            "Dodawanie znajomych",
            "Zakupy w internecie"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Dlaczego nie warto reagować agresją na agresję w internecie?",
        answers = listOf(
            "Bo to jest trudne",
            "Bo to eskaluje konflikt",
            "Bo ludzie nie przeczytają",
            "Bo nie można"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co oznacza, że 'internet nie zapomina'?",
        answers = listOf(
            "Internet zapisuje tylko ważne rzeczy",
            "Dane zostają na zawsze w sieci",
            "Zdjęcia się usuwają automatycznie",
            "Posty są ukrywane po czasie"
        ),
        correctAnswer = 1
    ),


    QuizQuestion(
        question = "Co oznacza termin phishing?",
        answers = listOf(
            "Wysyłanie fałszywych wiadomości w celu wyłudzenia informacji",
            "Gra polegająca na łowieniu ryb wirtualnych",
            "Tworzenie fałszywych kont w grach",
            "Udostępnianie zdjęć online"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Jak można rozpoznać wiadomość phishingową?",
        answers = listOf(
            "Brak literówek i wysokiej jakości grafiki",
            "E-mail pochodzi od znajomej osoby",
            "Wiadomość jest zawsze podpisana pełnym imieniem",
            "Temat wymusza szybkie działanie"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Dlaczego nie należy klikać w linki w wiadomościach phishingowych?",
        answers = listOf(
            "Mogą zawierać nieprzyzwoite treści",
            "Są bezużyteczne",
            "Mogą prowadzić do gier online",
            "Mogą zainfekować komputer",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Jakie są potencjalne niebezpieczeństwa w grach online?",
        answers = listOf("Pomoc w nauce", "Rozwój umiejętności miękkich", "Kradzież tożsamości", "Trening fizyczny"),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co oznacza termin mikropłatności?",
        answers = listOf(
            "Drobne opłaty za dodatkowe funkcje w grze",
            "Bezpłatne dodatki w grach",
            "Zarabianie na grze",
            "Opłaty za internet"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Dlaczego nie należy podawać swojego hasła innym osobom?",
        answers = listOf(
            "Hasła są niepotrzebne w grach",
            "Można je łatwo zmienić",
            "Ktoś może przejąć Twoje konto",
            "Hasła są publiczne"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Jak można zabezpieczyć swoje konto w grach online?",
        answers = listOf(
            "Korzystając z prostych haseł",
            "Używając pełnego imienia",
            "Publikując swoje dane",
            "Wylogowując się po grze"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Co to jest uzależnienie od gier?",
        answers = listOf(
            "Brak chęci do gry",
            "Zabawa z przyjaciółmi",
            "Nadmierne granie z negatywnymi konsekwencjami",
            "Rozwój umiejętności logicznych"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Jakie działania mogą prowadzić do kradzieży tożsamości w sieci?",
        answers = listOf("Podanie pseudonimu", "Klikanie w nieznane linki", "Wyłączenie gry", "Używanie awatara"),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Dlaczego warto korzystać z menedżerów haseł?",
        answers = listOf(
            "Są trudne w użyciu",
            "Zabezpieczają dane i są wygodne",
            "Zapamiętują tylko jedno hasło",
            "Wymagają dodatkowych opłat"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co powinieneś zrobić po zakończeniu gry online?",
        answers = listOf("Zostawić grę włączoną", "Wylogować się", "Podzielić się hasłem", "Zaprosić znajomych"),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Co to jest zasada free-to-play?",
        answers = listOf(
            "Gra dostępna za darmo, ale z mikropłatnościami",
            "Gra, która jest zawsze płatna",
            "Gra bez reklam",
            "Gra dla dwóch graczy"
        ),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Jakie funkcje w ustawieniach prywatności warto sprawdzić?",
        answers = listOf("Kalibrację ekranu", "Dostęp do mikrofonu i kamery", "Rodzaj grafiki", "Czas gry"),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Kto powinien zakładać konto, jeśli gracz ma poniżej 13 lat?",
        answers = listOf("Sam gracz", "Znajomi", "Rodzice lub opiekunowie", "Nauczyciele"),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co oznacza netykieta w grach online?",
        answers = listOf("Zasady kultury i szacunku w sieci", "Styl gry", "Rodzaj ustawień", "Czas rozgrywki"),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Dlaczego nie powinieneś ustawiać swojego zdjęcia jako awatar?",
        answers = listOf(
            "To jest trudne do zrobienia",
            "Bezpieczniejsze jest użycie grafiki lub awatara",
            "Zdjęcia są zawsze potrzebne",
            "Nie wpływa to na prywatność"
        ),
        correctAnswer = 1
    ),
    QuizQuestion(
        question = "Jakie są konsekwencje uzależnienia od gier?",
        answers = listOf(
            "Rozwój umiejętności",
            "Poprawa zdrowia",
            "Zaniedbanie obowiązków i relacji",
            "Więcej wolnego czasu"
        ),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Co należy zrobić, jeśli zauważysz sygnały uzależnienia od gier?",
        answers = listOf("Ignorować je", "Grać dalej", "Szukaj pomocy i wsparcia", "Usunąć konto"),
        correctAnswer = 2
    ),
    QuizQuestion(
        question = "Dlaczego warto używać pseudonimu w grach online?",
        answers = listOf(
            "Jest bardziej stylowy",
            "Jest wymagany w każdej grze",
            "Pozwala na lepsze wyniki",
            "Chroni Twoją tożsamość",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Co zrobić, jeśli otrzymasz podejrzaną wiadomość e-mail?",
        answers = listOf(
            "Kliknij link i sprawdź",
            "Podaj swoje dane",
            "Zignoruj ostrzeżenia",
            "Usuń wiadomość lub zgłoś ją",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Dlaczego ważne jest wylogowanie się z gry na cudzym urządzeniu?",
        answers = listOf(
            "Nie ma takiej potrzeby",
            "Jest to trudne",
            "Wymaga hasła",
            "Chroni Twoje konto przed dostępem innych osób",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Jak można poprawić bezpieczeństwo swojego konta?",
        answers = listOf(
            "Udostępniając hasło",
            "Logując się z cudzych urządzeń",
            "Klikając każdy link",
            "Używając unikalnych haseł, najlepiej korzystać z ich menadżera"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Jakie działania mogą prowadzić do infekcji komputera?",
        answers = listOf("Klikanie w załączniki e-mail", "Używanie pseudonimu", "Granie w gry", "Wylogowywanie się"),
        correctAnswer = 0
    ),
    QuizQuestion(
        question = "Co warto robić, aby uniknąć oszustw online?",
        answers = listOf(
            "Podawaj pełne dane",
            "Klikaj każdy załącznik",
            "Używaj jednego hasła",
            "Sprawdzaj nadawcę wiadomości"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Dlaczego warto grać w gry edukacyjne?",
        answers = listOf(
            "Są mniej interesujące",
            "Nie wymagają myślenia",
            "Nie mają żadnych zalet",
            "Pomagają rozwijać umiejętności",
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Jak można rozpoznać podejrzane wiadomości e-mail?",
        answers = listOf(
            "Brak literówek",
            "Profesjonalna grafika",
            "Zawsze wysyłane przez bank",
            "Ogólnikowe stwierdzenia i błędy językowe"
        ),
        correctAnswer = 3
    ),
    QuizQuestion(
        question = "Czym są menedżery haseł?",
        answers = listOf(
            "Programami służącymi do automatycznego tworzenia kont w mediach społecznościowych.",
            "Aplikacjami do śledzenia aktywności znajomych w internecie.",
            "Urządzeniami służącymi do zapamiętywania loginów i haseł.",
            "Narzędziami, które przechowują i pomagają zarządzać bezpiecznie hasłami użytkownika."
        ),
        correctAnswer = 3
    )


)
