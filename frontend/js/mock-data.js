const USUARIOS_MOCK = [
  { id: 1, nombre: "Ana",   correo: "ciudadano@ejemplo.com",   contrasena: "demo1234", rol: "ciudadano" },
  { id: 2, nombre: "Luis",  correo: "funcionario@ejemplo.com", contrasena: "demo1234", rol: "funcionario", funcionarioId: 1 },
  { id: 3, nombre: "Marta", correo: "admin@ejemplo.com",       contrasena: "demo1234", rol: "administrador" }
];

const FUNCIONARIOS_MOCK = [
  { id: 1, nombre: "Luis", apellido: "Pérez", correo: "funcionario@ejemplo.com", cargo: "Inspector de obras" }
];

const CATEGORIAS_MOCK = [
  { id: 1, nombre: "Vías",       descripcion: "Huecos y daños en la calle" },
  { id: 2, nombre: "Alumbrado",  descripcion: "Postes sin luz" },
  { id: 3, nombre: "Agua",       descripcion: "Fugas y daños en tuberías" }
];

const REPORTES_MOCK = [
  {
    id: 1,
    titulo: "Hueco grande en la calle",
    descripcion: "Hueco de unos 50 cm que dificulta el paso de carros.",
    categoria: CATEGORIAS_MOCK[0],
    ubicacion: "Calle 10 con Carrera 5",
    estado: "PENDIENTE",
    usuario: { id: 1, nombre: "Ana", correo: "ciudadano@ejemplo.com", rol: "ciudadano" },
    funcionario: null,
    fechaCreacion: "2026-11-10T09:30:00",
    fechaActualizacion: "2026-11-10T09:30:00"
  },
  {
    id: 2,
    titulo: "Poste sin luz",
    descripcion: "El poste de la esquina lleva una semana apagado.",
    categoria: CATEGORIAS_MOCK[1],
    ubicacion: "Carrera 8 con Calle 20",
    estado: "EN_PROCESO",
    usuario: { id: 1, nombre: "Ana", correo: "ciudadano@ejemplo.com", rol: "ciudadano" },
    funcionario: { id: 1, nombre: "Luis" },
    fechaCreacion: "2026-11-08T14:00:00",
    fechaActualizacion: "2026-11-11T08:15:00"
  }
];