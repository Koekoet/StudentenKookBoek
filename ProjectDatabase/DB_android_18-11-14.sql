-- phpMyAdmin SQL Dump
-- version 4.0.4.2
-- http://www.phpmyadmin.net
--
-- Machine: localhost
-- Genereertijd: 18 nov 2014 om 19:37
-- Serverversie: 5.6.13
-- PHP-versie: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Databank: `androidproject`
--
CREATE DATABASE IF NOT EXISTS `androidproject` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `androidproject`;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_account`
--

CREATE TABLE IF NOT EXISTS `ap_account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_difficulty_recept`
--

CREATE TABLE IF NOT EXISTS `ap_difficulty_recept` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Gegevens worden uitgevoerd voor tabel `ap_difficulty_recept`
--

INSERT INTO `ap_difficulty_recept` (`ID`, `Description`) VALUES
(1, 'Beginner'),
(2, 'Gevorderde'),
(3, 'Professional'),
(4, 'Instructeur');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_ingredient`
--

CREATE TABLE IF NOT EXISTS `ap_ingredient` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `AllowedUnits` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Gegevens worden uitgevoerd voor tabel `ap_ingredient`
--

INSERT INTO `ap_ingredient` (`ID`, `Name`, `AllowedUnits`) VALUES
(1, 'Aardappelen', '1;2;5'),
(2, 'Gehakt', '1;2'),
(3, 'Bloemkool', '1;2'),
(4, 'Boter', '1;2'),
(5, 'Water', '3;4'),
(6, 'Tomaat', '1;2;5');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_profile`
--

CREATE TABLE IF NOT EXISTS `ap_profile` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AccountId` int(11) NOT NULL,
  `Picture` int(11) NOT NULL,
  `ReceptIds` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_recept_category`
--

CREATE TABLE IF NOT EXISTS `ap_recept_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Picture` varchar(100) NOT NULL,
  `Name` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Gegevens worden uitgevoerd voor tabel `ap_recept_category`
--

INSERT INTO `ap_recept_category` (`ID`, `Picture`, `Name`) VALUES
(1, '', 'Desserts'),
(2, '', 'Vleesgerechten'),
(3, '', 'Visgerechten'),
(4, '', 'Thaise keuken'),
(5, '', 'Chinese keuken'),
(6, '', 'Vegetarisch');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_recipe`
--

CREATE TABLE IF NOT EXISTS `ap_recipe` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Recipename` varchar(100) NOT NULL,
  `AuthorId` int(11) NOT NULL,
  `Duration` varchar(100) NOT NULL,
  `Cost` varchar(100) NOT NULL,
  `NumberOfPersons` int(11) NOT NULL,
  `DifficultyId` int(11) NOT NULL,
  `Picture` varchar(100) NOT NULL,
  `Ingredients` varchar(100) NOT NULL,
  `RecipeText` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Gegevens worden uitgevoerd voor tabel `ap_recipe`
--

INSERT INTO `ap_recipe` (`ID`, `Recipename`, `AuthorId`, `Duration`, `Cost`, `NumberOfPersons`, `DifficultyId`, `Picture`, `Ingredients`, `RecipeText`) VALUES
(1, 'Ovenschotel met gehakt', 0, '30', '< €5,00', 4, 1, '', '1;2;3;4;5', 'Stap 1:\r\nZet twee potten op het vuur. Vul ze met het water en laat het koken.\r\nEens het water kookt,');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_recipes_by_category`
--

CREATE TABLE IF NOT EXISTS `ap_recipes_by_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryId` int(11) NOT NULL,
  `RecipeIds` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `ap_unit`
--

CREATE TABLE IF NOT EXISTS `ap_unit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Abbreviation` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Gegevens worden uitgevoerd voor tabel `ap_unit`
--

INSERT INTO `ap_unit` (`ID`, `Name`, `Abbreviation`) VALUES
(1, 'Kilogram', 'kg'),
(2, 'Gram', 'g'),
(3, 'Liter', 'l'),
(4, 'Deciliter', 'dl'),
(5, 'Stuk', 'st');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
