package com.musika.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.auxiliar.LoginAuxiliar;
import com.musika.backend.model.Roles;
import com.musika.backend.model.Usuarios;
import com.musika.backend.repository.UsuariosRepository;

@Service
public class UsuarioServiceImp implements UsuarioService {

    private static final String PIMIENTA = "Galea";

    @Autowired
    private UsuariosRepository useRepo;

    @Override
    public Usuarios buscarPorUser(String user) {
        try {
            return useRepo.findByUser(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Usuarios insertar(Usuarios usu) {
        try {
            if (usu == null || usu.getUser() == null || usu.getUser().isBlank() || usu.getPass() == null
                    || usu.getPass().isBlank()) {
                return null;
            }

            if (useRepo.findByUser(usu.getUser()) != null) {
                return null;
            }

            String sal = LoginAuxiliar.generarHexRandom(16);
            Usuarios usuarioTemp = new Usuarios();

            usuarioTemp.setUser(usu.getUser());
            usuarioTemp.setPass(LoginAuxiliar.hashear(usu.getPass() + sal));

            usuarioTemp.setSalt(sal);
            usuarioTemp.setIntentos(0);
            usuarioTemp.setFoto(
                    usu.getFoto() == null || usu.getFoto().isBlank() ? "default.jpg" : usu.getFoto().trim());
            usuarioTemp.setRolFK(new Roles(2, null));

            return useRepo.save(usuarioTemp);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Usuarios> obtenerTodos() {
        List<Usuarios> listaUsuarios = null;
        try {
            listaUsuarios = useRepo.findAll();
        } catch (Exception e) {

            return null;
        }

        return listaUsuarios;
    }

    @Override
    public Boolean modificar(Usuarios aut) {
        try {

            if (useRepo.existsById(aut.getId())) {
                useRepo.save(aut);
                return true;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Boolean borrar(Integer id) {
        try {
            if (useRepo.existsById(id)) {
                useRepo.deleteById(id);
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
    public Boolean existeUsuario(Integer id) {
        try {
            return useRepo.existsById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Usuarios obtenerUsuarioById(Integer id) {
        Usuarios listaUse = null;
        try {
            listaUse = useRepo.findById(id).orElse(new Usuarios());
        } catch (Exception e) {

            return null;
        }

        return listaUse;
    }

    @Override
    public boolean validarPassword(String password, Usuarios usuario) {
        try {
            String primerHash = password;

            String hashFinal = LoginAuxiliar.hashear(primerHash + usuario.getSalt());

            byte[] hashCalculado = hashFinal.getBytes(StandardCharsets.UTF_8);
            byte[] hashGuardado = usuario.getPass()
                    .toLowerCase()
                    .getBytes(StandardCharsets.UTF_8);

            return MessageDigest.isEqual(hashCalculado, hashGuardado);

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean bloquearUsuario(Integer id) {
        try {
            Usuarios usuario = useRepo.findById(id).orElse(null);

            if (usuario == null) {
                return false;
            }

            usuario.setRolFK(new Roles(3, null));
            useRepo.save(usuario);

            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Boolean gestionarRolUsuarios(Integer idusu, Integer idRol) {
        try {
            Usuarios usuario = useRepo.findById(idusu).orElse(null);

            if (usuario == null) {
                return false;
            }

            usuario.setRolFK(new Roles(idRol, null));
            useRepo.save(usuario);

            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Boolean bannearUsuario(Integer id) {
        try {
            Usuarios usuario = useRepo.findById(id).orElse(null);

            if (usuario == null) {
                return false;
            }

            usuario.setRolFK(new Roles(4, null));
            useRepo.save(usuario);

            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public Boolean incrementarIntentos(Integer id) {
        try {
            Usuarios usuario = useRepo.findById(id).orElse(null);

            if (usuario == null) {
                return false;
            }

            Integer intentosUSer = usuario.getIntentos();

            usuario.setIntentos(intentosUSer + 1);
            useRepo.save(usuario);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public Boolean reiniciarIntentos(Integer id) {
        try {
            Usuarios usuario = useRepo.findById(id).orElse(null);

            if (usuario == null) {
                return false;
            }

            usuario.setIntentos(0);
            useRepo.save(usuario);

            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

}
