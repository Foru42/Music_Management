package com.musika.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.model.Canciones;
import com.musika.backend.repository.CancionesRepository;

@Service
public class CancionesServiceImp implements CancionesService {
    @Autowired
    private CancionesRepository cancionRepo;

    @Override
    public List<Canciones> obtenerTodosConRolPermitido() {
        List<Canciones> listaCanciones = null;

        try {
            listaCanciones = cancionRepo.findByRolesPermitidos();
        } catch (Exception e) {

            return null;
        }

        return listaCanciones;
    }

    @Override
    public Boolean existeCanciones(Integer id) {
        try {
            return cancionRepo.existsById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Canciones obtenerCancionById(Integer id) {
        Canciones listaOrde = null;
        try {
            listaOrde = cancionRepo.findById(id).orElse(new Canciones());
        } catch (Exception e) {

            return null;
        }

        return listaOrde;
    }

    @Override
    public Canciones insertar(Canciones aut) {
        Canciones listaCanciones = null;

        try {
            if (aut != null && aut.getId() == null) {
                listaCanciones = cancionRepo.save(aut);
                return listaCanciones;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean modificar(Canciones datos) {
        try {
            if (datos.getId() == null) {
                return false;
            }

            Canciones existente = cancionRepo.findById(datos.getId()).orElse(null);

            if (existente == null) {
                return false;
            }

            if (datos.getTitulo() != null) {
                existente.setTitulo(datos.getTitulo());
            }

            if (datos.getEnlace() != null) {
                existente.setEnlace(datos.getEnlace());
            }

            if (datos.getDuracion() != null) {
                existente.setDuracion(datos.getDuracion());
            }

            if (datos.getDescripcion() != null) {
                existente.setDescripcion(datos.getDescripcion());
            }

            if (datos.getEstiloFK() != null && datos.getEstiloFK().getId() != null) {
                existente.setEstiloFK(datos.getEstiloFK());
            }

            if (datos.getReportada() != null) {
                existente.setReportada(datos.getReportada());
            }

            cancionRepo.save(existente);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean borrar(Integer id) {
        try {
            if (cancionRepo.existsById(id)) {
                cancionRepo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public List<Canciones> buscarCancionMultiFiltro(String usuario, String estilo, Double min, Double max,
            Boolean reportada) {

        List<Canciones> listacanciones = null;
        try {
            listacanciones = cancionRepo.filtrarAdmin(usuario, estilo, min, max, reportada);
        } catch (Exception e) {
            return null;
        }

        return listacanciones;
    }

}
