// Servicio de autenticación para gestionar sesiones


export interface User {
  id: number;
  name: string;
  email: string;
  idRole: number | null;
}

export interface LoginResponse {
  id: number;
  name: string;
  email: string;
  idRole: number | null;
  status: string;
  message: string;
  redirect?: string;
}

export interface RegisterResponse {
  id: number;
  name: string;
  email: string;
  idRole: number | null;
  message: string;
}

export interface ErrorResponse {
  message: string;
}

class AuthService {
  private readonly STORAGE_KEY = 'user';
  private readonly BASE_URL = import.meta.env.VITE_API_URL ?? ''; // Vite comuniccion con backend

  /**
   * Registrar nuevo usuario
   */
  async register(name: string, email: string, password: string): Promise<RegisterResponse> {
    const response = await fetch(`${this.BASE_URL}/api/login/register`, { // 👈 cambia
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
       credentials: "include",
      body: JSON.stringify({ name, email, password }),
    });

    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(error.message || 'Error en el registro');
    }

    const data: RegisterResponse = await response.json();
    
    // Guardar usuario en localStorage
    this.saveUser({
      id: data.id,
      name: data.name,
      email: data.email,
      idRole: data.idRole,
    });

    return data;
  }

  /**
   * Iniciar sesión
   */
  async login(email: string, password: string): Promise<LoginResponse> {
    const response = await fetch(`${this.BASE_URL}/api/login/loginUser`, { // 👈 cambia
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
       credentials: "include",
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new Error(error.message || 'Error al iniciar sesión');
    }

    const data: LoginResponse = await response.json();

    // Guardar usuario en localStorage
    if (data.status === 'success') {
      this.saveUser({
        id: data.id,
        name: data.name,
        email: data.email,
        idRole: data.idRole,
      });
    }

    return data;
  }

  /**
   * Cerrar sesión
   */
  async logout(): Promise<void> {
    try {
      await fetch(`${this.BASE_URL}/api/login/logout`, { // 👈 cambia
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
         credentials: "include",
      });
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
    } finally {
      this.clearUser();
    }
  }

  /**
   * Verificar sesión activa en el servidor
   */
  async checkSession(): Promise<LoginResponse | null> {
    try {
      const response = await fetch(`${this.BASE_URL}/api/login/session`, { // 👈 cambia
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
         credentials: "include",
      });

      if (!response.ok) {
        return null;
      }

      const data: LoginResponse = await response.json();
      
      // Actualizar localStorage con datos de sesión
      this.saveUser({
        id: data.id,
        name: data.name,
        email: data.email,
        idRole: data.idRole,
      });

      return data;
    } catch (error) {
      console.error('Error al verificar sesión:', error);
      return null;
    }
  }

  /**
   * Obtener usuario actual del localStorage
   */
  getCurrentUser(): User | null {
    const userStr = localStorage.getItem(this.STORAGE_KEY);
    if (!userStr) return null;

    try {
      return JSON.parse(userStr) as User;
    } catch {
      return null;
    }
  }

  /**
   * Verificar si el usuario está autenticado
   */
  isAuthenticated(): boolean {
    return this.getCurrentUser() !== null;
  }

  /**
   * Verificar si el usuario es administrador
   */
  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.idRole === 1;
  }

  /**
   * Guardar usuario en localStorage
   */
  private saveUser(user: User): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
  }

  /**
   * Limpiar usuario del localStorage
   */
  private clearUser(): void {
    localStorage.removeItem(this.STORAGE_KEY);
  }
}

// Exportar instancia única del servicio
const authService = new AuthService();
export default authService;
