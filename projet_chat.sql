-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 12 jan. 2023 à 20:55
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `projet_chat`
--

-- --------------------------------------------------------

--
-- Structure de la table `amis`
--

CREATE TABLE `amis` (
  `id_clt1` int(11) NOT NULL,
  `id_clt2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `amis`
--

INSERT INTO `amis` (`id_clt1`, `id_clt2`) VALUES
(2, 3),
(2, 5),
(2, 12),
(3, 5),
(5, 10),
(12, 3),
(12, 5),
(12, 10),
(13, 10),
(13, 12),
(15, 2),
(15, 14),
(16, 5),
(16, 10),
(16, 15);

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id_clt` int(11) NOT NULL,
  `login_clt` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id_clt`, `login_clt`, `password`) VALUES
(2, 'amina', '123'),
(3, 'kawkaw', 'kao123'),
(5, 'amal', 'amal123'),
(10, 'khadija', '1234'),
(12, 'halima', 'ha123'),
(13, 'najwa', '123'),
(14, 'rania', '123'),
(15, 'kaoutar', '123'),
(16, 'lobna', '123');

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id_message` int(11) NOT NULL,
  `message` text NOT NULL,
  `sender` int(11) NOT NULL,
  `receiver` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `message`
--

INSERT INTO `message` (`id_message`, `message`, `sender`, `receiver`) VALUES
(51, 'Cv', 2, 12),
(61, 'Lhmd lilah', 12, 5),
(62, 'Bonjour ', 2, 15),
(63, 'ça va', 2, 15),
(66, 'Parfait', 2, 15),
(68, 'Hi', 2, 15),
(69, 'Salut', 15, 2),
(72, 'Merci', 2, 15),
(73, 'Hi', 15, 2),
(74, 'Hi', 15, 14),
(75, 'Hi', 14, 15),
(78, 'Cv', 15, 2),
(79, 'Cv', 15, 14),
(82, 'Bonjour', 16, 10),
(83, 'Bonjour', 16, 15);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `amis`
--
ALTER TABLE `amis`
  ADD PRIMARY KEY (`id_clt1`,`id_clt2`),
  ADD KEY `fk_2` (`id_clt2`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id_clt`),
  ADD UNIQUE KEY `login_clt` (`login_clt`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `fk_ms1` (`sender`),
  ADD KEY `fk_ms2` (`receiver`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id_clt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id_message` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=84;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `amis`
--
ALTER TABLE `amis`
  ADD CONSTRAINT `fk_1` FOREIGN KEY (`id_clt1`) REFERENCES `client` (`id_clt`),
  ADD CONSTRAINT `fk_2` FOREIGN KEY (`id_clt2`) REFERENCES `client` (`id_clt`);

--
-- Contraintes pour la table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `fk_ms1` FOREIGN KEY (`sender`) REFERENCES `client` (`id_clt`),
  ADD CONSTRAINT `fk_ms2` FOREIGN KEY (`receiver`) REFERENCES `client` (`id_clt`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
