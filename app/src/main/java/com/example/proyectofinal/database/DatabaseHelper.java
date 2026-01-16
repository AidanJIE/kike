package com.example.proyectofinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.proyectofinal.models.Libro;
import com.example.proyectofinal.models.Venta;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Información de la base de datos
    private static final String DATABASE_NAME = "libreria.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de libros
    public static final String TABLE_LIBROS = "libros";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_AUTOR = "autor";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_CATEGORIA = "categoria";

    // Tabla de ventas
    public static final String TABLE_VENTAS = "ventas";
    public static final String COLUMN_VENTA_ID = "venta_id";
    public static final String COLUMN_ID_LIBRO = "id_libro";
    public static final String COLUMN_TITULO_LIBRO = "titulo_libro";
    public static final String COLUMN_CANTIDAD_VENTA = "cantidad";
    public static final String COLUMN_PRECIO_UNITARIO = "precio_unitario";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_FECHA_VENTA = "fecha";
    public static final String COLUMN_CLIENTE = "cliente";

    // Crear tabla de libros
    private static final String CREATE_TABLE_LIBROS =
            "CREATE TABLE " + TABLE_LIBROS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITULO + " TEXT NOT NULL," +
                    COLUMN_AUTOR + " TEXT NOT NULL," +
                    COLUMN_ISBN + " TEXT," +
                    COLUMN_PRECIO + " REAL NOT NULL," +
                    COLUMN_CANTIDAD + " INTEGER NOT NULL," +
                    COLUMN_CATEGORIA + " TEXT" +
                    ");";

    // Crear tabla de ventas
    private static final String CREATE_TABLE_VENTAS =
            "CREATE TABLE " + TABLE_VENTAS + "(" +
                    COLUMN_VENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ID_LIBRO + " INTEGER," +
                    COLUMN_TITULO_LIBRO + " TEXT NOT NULL," +
                    COLUMN_CANTIDAD_VENTA + " INTEGER NOT NULL," +
                    COLUMN_PRECIO_UNITARIO + " REAL NOT NULL," +
                    COLUMN_TOTAL + " REAL NOT NULL," +
                    COLUMN_FECHA_VENTA + " TEXT NOT NULL," +
                    COLUMN_CLIENTE + " TEXT" +
                    ");";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas
        db.execSQL(CREATE_TABLE_LIBROS);
        db.execSQL(CREATE_TABLE_VENTAS);
        Log.d("DatabaseHelper", "✅ Tablas creadas exitosamente");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambiamos la versión, eliminamos y recreamos
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBROS);
        onCreate(db);
    }

    // ============ MÉTODOS PARA LIBROS ============

    /**
     * Agregar un nuevo libro a la base de datos
     */
    public long agregarLibro(Libro libro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, libro.getTitulo());
        values.put(COLUMN_AUTOR, libro.getAutor());
        values.put(COLUMN_ISBN, libro.getIsbn());
        values.put(COLUMN_PRECIO, libro.getPrecio());
        values.put(COLUMN_CANTIDAD, libro.getCantidad());
        values.put(COLUMN_CATEGORIA, libro.getCategoria());

        long id = db.insert(TABLE_LIBROS, null, values);
        db.close();

        Log.d("DatabaseHelper", "📚 Libro agregado con ID: " + id);
        return id;
    }

    /**
     * Obtener todos los libros de la base de datos
     */
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> listaLibros = new ArrayList<>();

        // Consulta SQL
        String selectQuery = "SELECT * FROM " + TABLE_LIBROS +
                " ORDER BY " + COLUMN_TITULO + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Recorrer todos los registros
        if (cursor.moveToFirst()) {
            do {
                Libro libro = new Libro();
                libro.setId(cursor.getInt(0));          // id
                libro.setTitulo(cursor.getString(1));   // titulo
                libro.setAutor(cursor.getString(2));    // autor
                libro.setIsbn(cursor.getString(3));     // isbn
                libro.setPrecio(cursor.getDouble(4));   // precio
                libro.setCantidad(cursor.getInt(5));    // cantidad
                libro.setCategoria(cursor.getString(6)); // categoria

                listaLibros.add(libro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaLibros;
    }

    /**
     * Buscar libros por título, autor, categoría o ISBN
     */
    public List<Libro> buscarLibros(String texto) {
        List<Libro> listaLibros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_LIBROS + " WHERE " +
                COLUMN_TITULO + " LIKE ? OR " +
                COLUMN_AUTOR + " LIKE ? OR " +
                COLUMN_CATEGORIA + " LIKE ? OR " +
                COLUMN_ISBN + " LIKE ?" +
                " ORDER BY " + COLUMN_TITULO + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{
                "%" + texto + "%",  // Buscar título
                "%" + texto + "%",  // Buscar autor
                "%" + texto + "%",  // Buscar categoría
                "%" + texto + "%"   // Buscar ISBN
        });

        if (cursor.moveToFirst()) {
            do {
                Libro libro = new Libro();
                libro.setId(cursor.getInt(0));
                libro.setTitulo(cursor.getString(1));
                libro.setAutor(cursor.getString(2));
                libro.setIsbn(cursor.getString(3));
                libro.setPrecio(cursor.getDouble(4));
                libro.setCantidad(cursor.getInt(5));
                libro.setCategoria(cursor.getString(6));

                listaLibros.add(libro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaLibros;
    }

    /**
     * Actualizar un libro existente
     */
    public int actualizarLibro(Libro libro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, libro.getTitulo());
        values.put(COLUMN_AUTOR, libro.getAutor());
        values.put(COLUMN_ISBN, libro.getIsbn());
        values.put(COLUMN_PRECIO, libro.getPrecio());
        values.put(COLUMN_CANTIDAD, libro.getCantidad());
        values.put(COLUMN_CATEGORIA, libro.getCategoria());

        int result = db.update(TABLE_LIBROS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(libro.getId())});

        db.close();
        Log.d("DatabaseHelper", "📝 Libro actualizado: " + libro.getTitulo());
        return result;
    }

    /**
     * Eliminar un libro por su ID
     */
    public void eliminarLibro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIBROS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        Log.d("DatabaseHelper", "🗑️ Libro eliminado con ID: " + id);
    }

    /**
     * Obtener total de libros en la base de datos
     */
    public int obtenerTotalLibros() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_LIBROS;
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);

        cursor.close();
        db.close();
        return count;
    }

    /**
     * Obtener precio de un libro por su título
     */
    public double obtenerPrecioLibro(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PRECIO + " FROM " + TABLE_LIBROS +
                " WHERE " + COLUMN_TITULO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{titulo});

        double precio = 0;
        if (cursor.moveToFirst()) {
            precio = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return precio;
    }

    /**
     * Verificar stock disponible de un libro
     */
    public int obtenerStockLibro(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CANTIDAD + " FROM " + TABLE_LIBROS +
                " WHERE " + COLUMN_TITULO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{titulo});

        int stock = 0;
        if (cursor.moveToFirst()) {
            stock = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return stock;
    }

    /**
     * Buscar libro por título para obtener su ID
     */
    public int obtenerIdLibroPorTitulo(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_LIBROS +
                " WHERE " + COLUMN_TITULO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{titulo});

        int idLibro = -1; // -1 significa no encontrado
        if (cursor.moveToFirst()) {
            idLibro = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return idLibro;
    }

    /**
     * Actualizar stock de un libro después de una venta
     */
    public boolean actualizarStockLibro(String tituloLibro, int cantidadVendida) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 1. Obtener el ID del libro por título
            int idLibro = obtenerIdLibroPorTitulo(tituloLibro);
            if (idLibro == -1) {
                Log.d("DatabaseHelper", "❌ Libro no encontrado: " + tituloLibro);
                return false;
            }

            // 2. Obtener stock actual
            String queryStock = "SELECT " + COLUMN_CANTIDAD + " FROM " + TABLE_LIBROS +
                    " WHERE " + COLUMN_ID + " = ?";
            Cursor cursor = db.rawQuery(queryStock, new String[]{String.valueOf(idLibro)});

            if (cursor.moveToFirst()) {
                int stockActual = cursor.getInt(0);
                int nuevoStock = stockActual - cantidadVendida;

                // No permitir stock negativo
                if (nuevoStock < 0) {
                    nuevoStock = 0;
                }

                // 3. Actualizar en la base de datos
                ContentValues values = new ContentValues();
                values.put(COLUMN_CANTIDAD, nuevoStock);

                int filasActualizadas = db.update(TABLE_LIBROS, values,
                        COLUMN_ID + " = ?",
                        new String[]{String.valueOf(idLibro)});

                cursor.close();

                if (filasActualizadas > 0) {
                    Log.d("DatabaseHelper", "✅ Stock actualizado: " + tituloLibro +
                            " - Vendido: " + cantidadVendida +
                            " - Stock anterior: " + stockActual +
                            " - Nuevo stock: " + nuevoStock);
                    return true;
                }
            }

            cursor.close();
            return false;

        } catch (Exception e) {
            Log.e("DatabaseHelper", "❌ Error actualizando stock: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // ============ MÉTODOS PARA VENTAS ============

    /**
     * Agregar una nueva venta
     */
    public long agregarVenta(Venta venta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        double total = venta.getCantidad() * venta.getPrecioUnitario();

        values.put(COLUMN_TITULO_LIBRO, venta.getTituloLibro());
        values.put(COLUMN_CANTIDAD_VENTA, venta.getCantidad());
        values.put(COLUMN_PRECIO_UNITARIO, venta.getPrecioUnitario());
        values.put(COLUMN_TOTAL, total);
        values.put(COLUMN_FECHA_VENTA, venta.getFecha());
        values.put(COLUMN_CLIENTE, venta.getCliente());

        long idVenta = db.insert(TABLE_VENTAS, null, values);
        db.close();

        return idVenta;
    }




    /**
     * Obtener todas las ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> listaVentas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VENTAS +
                " ORDER BY " + COLUMN_FECHA_VENTA + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setId(cursor.getInt(0));  // venta_id
                venta.setTituloLibro(cursor.getString(2));  // titulo_libro
                venta.setCantidad(cursor.getInt(3));  // cantidad
                venta.setPrecioUnitario(cursor.getDouble(4));  // precio_unitario
                venta.setFecha(cursor.getString(6));  // fecha
                venta.setCliente(cursor.getString(7));  // cliente

                listaVentas.add(venta);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaVentas;
    }

    public boolean descontarStockPorTitulo(String titulo, int cantidadVendida) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + TABLE_LIBROS +
                " SET " + COLUMN_CANTIDAD + " = " + COLUMN_CANTIDAD + " - ?" +
                " WHERE " + COLUMN_TITULO + " = ? AND " + COLUMN_CANTIDAD + " >= ?";

        db.beginTransaction();
        try {
            db.execSQL(sql, new Object[]{cantidadVendida, titulo, cantidadVendida});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }




    /**
     * Obtener total de ingresos por ventas
     */
    public double obtenerIngresosTotales() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery = "SELECT SUM(" + COLUMN_TOTAL + ") FROM " + TABLE_VENTAS;
        Cursor cursor = db.rawQuery(sumQuery, null);

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    /**
     * Obtener cantidad total de ventas
     */
    public int obtenerTotalVentas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_VENTAS;
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);

        cursor.close();
        db.close();
        return count;
    }
}