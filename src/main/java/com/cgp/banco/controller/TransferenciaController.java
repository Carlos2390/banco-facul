package com.cgp.banco.controller;

import com.cgp.banco.dao.ContaRepository;
import com.cgp.banco.dao.TransferenciaRepository;
import com.cgp.banco.model.Cliente;
import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Transferencia;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    private final TransferenciaRepository transferenciaRepository;

    private final ContaRepository contaRepository;

    public TransferenciaController(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

    @PostMapping("/porNumeroContas")
    public ResponseEntity<?> criarTransferenciaPorNumeroContas(@RequestParam Integer numeroContaOrigem, @RequestParam Integer numeroContaDestino, @RequestParam Double valor, HttpSession session) {
            // Cria uma nova transferência
            Transferencia transferencia = new Transferencia();
            // Define o id da conta de origem
            Conta contaOrigem = contaDAO.buscarContaPorNumero(numeroContaOrigem);
            transferencia.setId_conta_origem(contaOrigem.getId());
            // Define o id da conta de destino
            Conta contaDesitino = contaDAO.buscarContaPorNumero(numeroContaDestino);
            transferencia.setId_conta_destino(contaDesitino.getId());
            // Define o valor da transferência
            transferencia.setValor(valor);

            transferencia.setData(LocalDateTime.now());
            
            transferenciaRepository.save(transferencia);

           
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarTransferencia(@RequestBody Transferencia transferencia, HttpSession session) {
            // Salva a transferência no banco de dados
            transferencia.setData(LocalDateTime.now());
            transferenciaRepository.save(transferencia);
         
            return ResponseEntity.ok("Transferência criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia, HttpSession session) {
        
        Transferencia transferenciaExistente = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferenciaExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            transferencia.setId(id);
            // Atualiza a transferência no banco de dados
            transferenciaDAO.atualizar(transferencia);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência atualizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTransferenciaPorId(@PathVariable Long id, HttpSession session) {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
               
                return ResponseEntity.notFound().build();
            }
            // Retorna a transferência encontrada
            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaOrigem(@RequestParam Integer numeroContaOrigem, HttpSession session) {
       
            Conta conta = contaRepository.findByNumero(numeroContaOrigem);
            List<Transferencia> transferencias = transferenciaRepository.findByIdContaOrigem(conta.getId());

            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaDestino(@RequestParam Integer numeroContaDestino, HttpSession session) {
       
            Conta conta = contaRepository.findByNumero(numeroContaDestino);

            List<Transferencia> transferencias = transferenciaRepository.findByIdContaDestino(conta.getId());
            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTransferencia(@PathVariable Long id, HttpSession session) {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
                return ResponseEntity.notFound().build();
            }
            
            transferenciaRepository.deleteById(id);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência deletada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar transferencia: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarTodasTransferencias(HttpSession session) {
       
            List<Transferencia> transferencias = transferenciaRepository.findAll();
            // Retorna a lista de transferências
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

}
