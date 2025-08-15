/**
 * Este arquivo configura o seu projeto Truffle.
 * Ele inclui configurações comuns para diferentes redes e funcionalidades como migrações,
 * compilação e testes.
 *
 * Mais informações sobre a configuração podem ser encontradas em:
 * https://trufflesuite.com/docs/truffle/reference/configuration
 */

// Importa a biblioteca dotenv para carregar variáveis de ambiente do arquivo .env
require('dotenv').config();

// Importa o HDWalletProvider para conectar sua carteira a redes remotas
const HDWalletProvider = require('@truffle/hdwallet-provider');

// Carrega a frase mnemônica ou a chave privada das variáveis de ambiente.
// É CRÍTICO NUNCA COLOCAR SUA CHAVE PRIVADA OU MNEMÔNICA DIRETAMENTE AQUI.
// Crie um arquivo .env na raiz do seu projeto (e adicione-o ao .gitignore)
// com o seguinte formato:
// MNEMONIC="sua frase mnemônica de 12 palavras aqui"
// PRIVATE_KEY="sua chave privada aqui (começando com 0x)"
const mnemonic = process.env.MNEMONIC;
const privateKey = process.env.PRIVATE_KEY;

module.exports = {
  /**
   * As redes definem como você se conecta ao seu cliente Ethereum
   * e permitem definir os padrões que o web3 usa para enviar transações.
   * Se você não especificar uma, o Truffle iniciará uma instância gerenciada do Ganache
   * na porta 9545 quando você executar `develop` ou `test`.
   * Você pode pedir a um comando Truffle para usar uma rede específica
   * na linha de comando, por exemplo:
   * $ truffle test --network <nome-da-rede>
   */
  networks: {
    // Rede de desenvolvimento local (Ganache)
    // Útil para testes. O nome `development` é especial - o Truffle o usa por padrão
    // se estiver definido aqui e nenhuma outra rede for especificada na linha de comando.
    development: {
      host: "127.0.0.1",     // Localhost (endereço padrão do Ganache)
      port: 8545,            // Porta padrão do Ganache UI (verifique a sua, pode ser 7545)
      network_id: "*",       // Qualquer ID de rede (o Ganache geralmente usa 5777 ou 1337)
    },

    // --- Redes da BNB Smart Chain (BSC) ---

    // BSC Testnet (Rede de Teste)
    // Use esta rede para testar seus contratos sem gastar BNB real.
    // Você pode obter BNB de teste em faucets da BSC: https://www.bnbchain.org/en/faucet
    bscTestnet: {
      provider: () => {
        // Verifica se a mnemônica ou chave privada está definida
        if (!mnemonic && !privateKey) {
            throw new Error("MNEMONIC ou PRIVATE_KEY não encontrado(a) no .env para bscTestnet.");
        }
        const providerUrl = `https://data-seed-prebsc-1-s1.bnbchain.org:8545`;
        // Prioriza a chave privada se estiver definida, senão usa a mnemônica
        return privateKey ? new HDWalletProvider(privateKey, providerUrl) : new HDWalletProvider(mnemonic, providerUrl);
      },
      network_id: 97,      // Chain ID da BSC Testnet
      confirmations: 2,    // Número de blocos a aguardar por confirmação
      timeoutBlocks: 200,  // Tempo limite para uma transação (em blocos)
      skipDryRun: true     // Pula a simulação de transação antes da migração real
    },

    // BSC Mainnet (Rede Principal)
    // Use esta rede para implantar seus contratos em produção.
    // Você precisará de BNB real para pagar as taxas de gás.
    bscMainnet: {
      provider: () => {
        // Verifica se a mnemônica ou chave privada está definida
        if (!mnemonic && !privateKey) {
            throw new Error("MNEMONIC ou PRIVATE_KEY não encontrado(a) no .env para bscMainnet.");
        }
        const providerUrl = `https://bsc-dataseed.bnbchain.org`;
        // Prioriza a chave privada se estiver definida, senão usa a mnemônica
        return privateKey ? new HDWalletProvider(privateKey, providerUrl) : new HDWalletProvider(mnemonic, providerUrl);
      },
      network_id: 56,      // Chain ID da BSC Mainnet
      confirmations: 10,   // Número de blocos a aguardar por confirmação (maior segurança)
      timeoutBlocks: 200,  // Tempo limite para uma transação (em blocos)
      skipDryRun: true     // Pula a simulação de transação antes da migração real
    },

    // --- Exemplo de rede Ethereum (se precisar no futuro) ---
    // Você pode descomentar e configurar outras redes aqui, como Ethereum Sepolia ou Mainnet,
    // lembrando de usar um serviço como Infura ou Alchemy e seu respectivo PROJECT_ID.
    // sepolia: {
    //   provider: () => {
    //     if (!mnemonic && !privateKey) {
    //         throw new Error("MNEMONIC ou PRIVATE_KEY não encontrado(a) no .env para Sepolia");
    //     }
    //     const providerUrl = `https://sepolia.infura.io/v3/${process.env.INFURA_PROJECT_ID}`;
    //     return privateKey ? new HDWalletProvider(privateKey, providerUrl) : new HDWalletProvider(mnemonic, providerUrl);
    //   },
    //   network_id: 11155111,
    //   confirmations: 2,
    //   timeoutBlocks: 200,
    //   skipDryRun: true
    // },
  },

  // Define as opções padrão do Mocha para testes
  mocha: {
    // timeout: 100000 // Exemplo de tempo limite para testes
  },

  // Configura seus compiladores Solidity
  compilers: {
    solc: {
      version: "0.8.10",    // Defina a versão exata do seu compilador Solidity.
                            // Certifique-se de que esta versão corresponde à versão
                            // definida nos seus arquivos de contrato Solidity (pragma solidity ^0.8.10;).
      settings: {
        optimizer: {
          enabled: true,    // Habilita o otimizador do compilador Solidity
          runs: 200         // Número de execuções do otimizador
        },
        // evmVersion: "byzantium" // Opcional: define a versão da EVM
      }
    }
  },

  // Truffle DB (atualmente desabilitado por padrão)
  // db: {
  //   enabled: false,
  //   host: "127.0.0.1",
  //   adapter: {
  //     name: "indexeddb",
  //     settings: {
  //       directory: ".db"
  //     }
  //   }
  // }
};
