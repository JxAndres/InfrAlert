const USAR_MOCK = true;

function usuarioActual() {
  try { return JSON.parse(sessionStorage.getItem("usuario")); }
  catch { return null; }
}

function cerrarSesion() {
  sessionStorage.removeItem("usuario");
  window.location.href = "index.html";
}

async function login(correo, contrasena) {
  if (USAR_MOCK) {
    const u = USUARIOS_MOCK.find(x => x.correo === correo && x.contrasena === contrasena);
    if (!u) throw new Error("Correo o contraseña incorrectos");
    const { contrasena: _omitida, ...usuario } = u; // nunca guardamos la contraseña
    sessionStorage.setItem("usuario", JSON.stringify(usuario));
    return usuario;
  }
  throw new Error("API real pendiente de contrato");
}

async function listarCategorias() {
  if (USAR_MOCK) return CATEGORIAS_MOCK;
  throw new Error("API real pendiente de contrato");
}

function _leerReportesMock() {
  const guardado = sessionStorage.getItem("reportes_mock");
  return guardado ? JSON.parse(guardado) : JSON.parse(JSON.stringify(REPORTES_MOCK));
}

function _guardarReportesMock(lista) {
  sessionStorage.setItem("reportes_mock", JSON.stringify(lista));
}

function _ahora() {
  const d = new Date();
  const p = n => String(n).padStart(2, "0");
  return d.getFullYear() + "-" + p(d.getMonth() + 1) + "-" + p(d.getDate()) + "T" +
         p(d.getHours()) + ":" + p(d.getMinutes()) + ":" + p(d.getSeconds());
}

async function listarMisReportes(usuarioId) {
  if (USAR_MOCK) {
    return _leerReportesMock().filter(r => r.usuario && r.usuario.id === usuarioId);
  }
  throw new Error("API real pendiente de contrato");
}

async function crearReporte({ titulo, descripcion, categoriaId, ubicacion }) {
  if (USAR_MOCK) {
    const lista = _leerReportesMock();
    const categoria = CATEGORIAS_MOCK.find(c => c.id === Number(categoriaId));
    if (!categoria) throw new Error("Categoría no válida");
    const nuevo = {
      id: lista.reduce((max, r) => Math.max(max, r.id), 0) + 1,
      titulo, descripcion, categoria, ubicacion,
      estado: "PENDIENTE",
      usuario: usuarioActual(),
      funcionario: null,
      fechaCreacion: _ahora(),
      fechaActualizacion: _ahora()
    };
    lista.push(nuevo);
    _guardarReportesMock(lista);
    return nuevo;
  }
  throw new Error("API real pendiente de contrato");
}

async function listarSinAsignar() {
  if (USAR_MOCK) {
    return _leerReportesMock().filter(r => !r.funcionario && r.estado === "PENDIENTE");
  }
  throw new Error("API real pendiente de contrato");
}

async function listarReportesDeFuncionario(funcionarioId) {
  if (USAR_MOCK) {
    return _leerReportesMock().filter(r => r.funcionario && r.funcionario.id === funcionarioId);
  }
  throw new Error("API real pendiente de contrato");
}

async function tomarReporte(reporteId, funcionarioId) {
  if (USAR_MOCK) {
    const lista = _leerReportesMock();
    const r = lista.find(x => x.id === reporteId);
    if (!r) throw new Error("Reporte no encontrado");
    if (r.funcionario) throw new Error("El Reporte " + reporteId + " ya está asignado a un funcionario");
    const ocupado = lista.some(x => x.funcionario && x.funcionario.id === funcionarioId && x.estado === "EN_PROCESO");
    if (ocupado) throw new Error("Ya tienes asignado un reporte. Debes terminarlo antes de tomar otro.");
    const f = FUNCIONARIOS_MOCK.find(x => x.id === funcionarioId);
    if (!f) throw new Error("Funcionario no encontrado");
    r.funcionario = { id: f.id, nombre: f.nombre };
    r.estado = "EN_PROCESO";
    r.fechaActualizacion = _ahora();
    _guardarReportesMock(lista);
    return r;
  }
  throw new Error("API real pendiente de contrato");
}

async function cambiarEstado(reporteId, nuevoEstado) {
  if (USAR_MOCK) {
    const validos = ["PENDIENTE", "EN_PROCESO", "RESUELTO", "CANCELADO"];
    if (!validos.includes(nuevoEstado)) throw new Error("Estado no válido");
    const lista = _leerReportesMock();
    const r = lista.find(x => x.id === reporteId);
    if (!r) throw new Error("Reporte no encontrado");
    if (r.estado === "RESUELTO" || r.estado === "CANCELADO") {
      throw new Error("El reporte ya se encuentra en un estado final");
    }
    const permitidos = r.estado === "PENDIENTE" ? ["EN_PROCESO", "CANCELADO"] : ["RESUELTO", "CANCELADO"];
    if (!permitidos.includes(nuevoEstado)) {
      throw new Error("No se puede cambiar de " + r.estado + " a " + nuevoEstado);
    }
    r.estado = nuevoEstado;
    r.fechaActualizacion = _ahora();
    _guardarReportesMock(lista);
    return r;
  }
  throw new Error("API real pendiente de contrato");
}
// --- Administrador (rutas reales: GET /api/reportes y GET /api/reportes/estadisticas) ---
async function listarTodosReportes() {
  if (USAR_MOCK) return _leerReportesMock();
  throw new Error("API real pendiente de contrato");
}

async function obtenerEstadisticas() {
  if (USAR_MOCK) {
    const lista = _leerReportesMock();
    const contar = e => lista.filter(r => r.estado === e).length;
    const agrupar = (extraer) => {
      const cuentas = {};
      lista.forEach(r => {
        const clave = extraer(r);
        if (clave) cuentas[clave] = (cuentas[clave] || 0) + 1;
      });
      return Object.entries(cuentas).map(([nombre, cantidad]) => ({ nombre, cantidad }));
    };
    return {
      total: lista.length,
      pendientes: contar("PENDIENTE"),
      enProceso: contar("EN_PROCESO"),
      resueltos: contar("RESUELTO"),
      cancelados: contar("CANCELADO"),
      sinAsignar: lista.filter(r => !r.funcionario).length,
      porCategoria: agrupar(r => r.categoria && r.categoria.nombre),
      porFuncionario: agrupar(r => r.funcionario && r.funcionario.nombre)
    };
  }
  throw new Error("API real pendiente de contrato");
}
const TIPOS_IMAGEN = ["image/jpeg", "image/png"];
const MAX_BYTES = 2 * 1024 * 1024;

function validarImagen(archivo) {
  if (!archivo) throw new Error("Debes seleccionar una imagen");
  if (archivo.size > MAX_BYTES) throw new Error("La imagen no puede superar los 2 MB");
  if (!TIPOS_IMAGEN.includes(archivo.type)) throw new Error("Solo se permiten imágenes JPG, JPEG o PNG");
}

async function subirEvidencia(reporteId, archivo) {
  validarImagen(archivo);
  if (USAR_MOCK) {
    const lista = _leerReportesMock();
    const r = lista.find(x => x.id === reporteId);
    if (!r) throw new Error("Reporte no encontrado");
    if (r.evidenciaNombre) throw new Error("Este reporte ya tiene una evidencia");
    r.evidenciaNombre = archivo.name; // en el mock solo se guarda el nombre, no los bytes
    _guardarReportesMock(lista);
    return r;
  }
  throw new Error("API real pendiente de contrato");
}