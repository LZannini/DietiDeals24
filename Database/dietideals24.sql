PGDMP                          |           dietideals24    15.4    15.3 U    W           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            X           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            Y           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            Z           1262    17409    dietideals24    DATABASE        CREATE DATABASE dietideals24 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE dietideals24;
                postgres    false            s           1247    17540 
   stato_asta    TYPE     V   CREATE TYPE public.stato_asta AS ENUM (
    'ATTIVA',
    'FALLITA',
    'VENDUTA'
);
    DROP TYPE public.stato_asta;
       public          postgres    false            [           1247    17411    tipo_utente    TYPE     ^   CREATE TYPE public.tipo_utente AS ENUM (
    'COMPRATORE',
    'VENDITORE',
    'COMPLETO'
);
    DROP TYPE public.tipo_utente;
       public          postgres    false            �            1255    17417    check_offerta_creatore()    FUNCTION     �  CREATE FUNCTION public.check_offerta_creatore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Controlla se l'utente è il creatore dell'asta
    IF EXISTS (
        SELECT 1
        FROM asta
        WHERE id = NEW.id_asta AND id_creatore = NEW.id_utente
    ) THEN
        RAISE EXCEPTION 'Un utente non può presentare un''offerta sulla propria asta';
    END IF;

    RETURN NEW;
END;
$$;
 /   DROP FUNCTION public.check_offerta_creatore();
       public          postgres    false            �            1255    17418    check_offerta_presente()    FUNCTION     U  CREATE FUNCTION public.check_offerta_presente() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM offerta
        WHERE id_utente = NEW.id_utente
        AND id_asta = NEW.id_asta
    ) THEN
        RAISE EXCEPTION 'L''utente ha già un''offerta in questa asta';
    END IF;
    RETURN NEW;
END;
$$;
 /   DROP FUNCTION public.check_offerta_presente();
       public          postgres    false            �            1255    25748    create_notifica()    FUNCTION     g  CREATE FUNCTION public.create_notifica() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    creator_id INT;
BEGIN
    SELECT id_creatore INTO creator_id FROM asta WHERE id = NEW.id_asta;

    INSERT INTO notifica (id_utente, testo, data, letta)
    VALUES (creator_id, 'Hai ricevuto una nuova offerta', CURRENT_DATE, false);

    RETURN NEW;
END;
$$;
 (   DROP FUNCTION public.create_notifica();
       public          postgres    false            �            1255    25745    set_asta_venduta_on_offerta()    FUNCTION     T  CREATE FUNCTION public.set_asta_venduta_on_offerta() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    asta_type TEXT;
BEGIN
    SELECT 'asta_ribasso' INTO asta_type
    FROM asta_ribasso
    WHERE id = NEW.id_asta;

    IF FOUND THEN
        UPDATE asta SET stato = 2 WHERE id = NEW.id_asta;
    END IF;

    RETURN NEW;
END;
$$;
 4   DROP FUNCTION public.set_asta_venduta_on_offerta();
       public          postgres    false            �            1255    17548    set_default_stato()    FUNCTION     �   CREATE FUNCTION public.set_default_stato() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.stato IS NULL THEN
        NEW.stato := 0;
    END IF;
    RETURN NEW;
END;
$$;
 *   DROP FUNCTION public.set_default_stato();
       public          postgres    false            �            1255    17550    set_timer_iniziale()    FUNCTION     �   CREATE FUNCTION public.set_timer_iniziale() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.timer_iniziale IS NULL THEN
        NEW.timer_iniziale := NEW.timer;
    END IF;
    RETURN NEW;
END;
$$;
 +   DROP FUNCTION public.set_timer_iniziale();
       public          postgres    false            �            1259    17419    asta    TABLE     :  CREATE TABLE public.asta (
    id integer NOT NULL,
    id_creatore integer NOT NULL,
    nome character varying(32) NOT NULL,
    descrizione character varying(256),
    categoria smallint NOT NULL,
    foto bytea,
    stato smallint NOT NULL,
    CONSTRAINT stato_check CHECK ((stato = ANY (ARRAY[0, 1, 2])))
);
    DROP TABLE public.asta;
       public         heap    postgres    false            �            1259    17424    asta_id_creatore_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_creatore_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.asta_id_creatore_seq;
       public          postgres    false    214            [           0    0    asta_id_creatore_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.asta_id_creatore_seq OWNED BY public.asta.id_creatore;
          public          postgres    false    215            �            1259    17425    asta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.asta_id_seq;
       public          postgres    false    214            \           0    0    asta_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.asta_id_seq OWNED BY public.asta.id;
          public          postgres    false    216            �            1259    17426    asta_inversa    TABLE     �   CREATE TABLE public.asta_inversa (
    id integer NOT NULL,
    prezzo double precision NOT NULL,
    scadenza character varying(255) NOT NULL
);
     DROP TABLE public.asta_inversa;
       public         heap    postgres    false            �            1259    17429    asta_inversa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_inversa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_inversa_id_asta_seq;
       public          postgres    false    217            ]           0    0    asta_inversa_id_asta_seq    SEQUENCE OWNED BY     P   ALTER SEQUENCE public.asta_inversa_id_asta_seq OWNED BY public.asta_inversa.id;
          public          postgres    false    218            �            1259    17430    asta_ribasso    TABLE     
  CREATE TABLE public.asta_ribasso (
    id integer NOT NULL,
    prezzo double precision NOT NULL,
    timer character varying(255) NOT NULL,
    decremento double precision NOT NULL,
    minimo double precision NOT NULL,
    timer_iniziale character varying(255)
);
     DROP TABLE public.asta_ribasso;
       public         heap    postgres    false            �            1259    17433    asta_ribasso_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_ribasso_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_ribasso_id_asta_seq;
       public          postgres    false    219            ^           0    0    asta_ribasso_id_asta_seq    SEQUENCE OWNED BY     P   ALTER SEQUENCE public.asta_ribasso_id_asta_seq OWNED BY public.asta_ribasso.id;
          public          postgres    false    220            �            1259    17434    asta_silenziosa    TABLE     o   CREATE TABLE public.asta_silenziosa (
    id integer NOT NULL,
    scadenza character varying(255) NOT NULL
);
 #   DROP TABLE public.asta_silenziosa;
       public         heap    postgres    false            �            1259    17437    asta_silenziosa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_silenziosa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.asta_silenziosa_id_asta_seq;
       public          postgres    false    221            _           0    0    asta_silenziosa_id_asta_seq    SEQUENCE OWNED BY     V   ALTER SEQUENCE public.asta_silenziosa_id_asta_seq OWNED BY public.asta_silenziosa.id;
          public          postgres    false    222            �            1259    17438    notifica    TABLE     �   CREATE TABLE public.notifica (
    id integer NOT NULL,
    id_utente integer NOT NULL,
    testo character varying(128) NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    letta boolean NOT NULL
);
    DROP TABLE public.notifica;
       public         heap    postgres    false            �            1259    25750    notifica_id_seq    SEQUENCE        CREATE SEQUENCE public.notifica_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 214483647
    CACHE 1;
 &   DROP SEQUENCE public.notifica_id_seq;
       public          postgres    false    223            `           0    0    notifica_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.notifica_id_seq OWNED BY public.notifica.id;
          public          postgres    false    230            �            1259    17441    offerta    TABLE     �   CREATE TABLE public.offerta (
    id_utente integer NOT NULL,
    id_asta integer NOT NULL,
    valore double precision NOT NULL,
    data character varying(255) NOT NULL,
    id integer NOT NULL
);
    DROP TABLE public.offerta;
       public         heap    postgres    false            �            1259    17444    offerta_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.offerta_id_asta_seq;
       public          postgres    false    224            a           0    0    offerta_id_asta_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.offerta_id_asta_seq OWNED BY public.offerta.id_asta;
          public          postgres    false    225            �            1259    17445    offerta_id_seq    SEQUENCE        CREATE SEQUENCE public.offerta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;
 %   DROP SEQUENCE public.offerta_id_seq;
       public          postgres    false    224            b           0    0    offerta_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.offerta_id_seq OWNED BY public.offerta.id;
          public          postgres    false    226            �            1259    17446    offerta_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.offerta_id_utente_seq;
       public          postgres    false    224            c           0    0    offerta_id_utente_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.offerta_id_utente_seq OWNED BY public.offerta.id_utente;
          public          postgres    false    227            �            1259    17447    utente    TABLE     T  CREATE TABLE public.utente (
    id integer NOT NULL,
    username character varying(32) NOT NULL,
    email character varying(64) NOT NULL,
    password character varying(32) NOT NULL,
    biografia character varying(256),
    sitoweb character varying(32),
    paese character varying(32),
    avatar bytea,
    tipo smallint NOT NULL
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �            1259    17452    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public          postgres    false    228            d           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public          postgres    false    229            �           2604    17453    asta id    DEFAULT     b   ALTER TABLE ONLY public.asta ALTER COLUMN id SET DEFAULT nextval('public.asta_id_seq'::regclass);
 6   ALTER TABLE public.asta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    214            �           2604    17454    asta id_creatore    DEFAULT     t   ALTER TABLE ONLY public.asta ALTER COLUMN id_creatore SET DEFAULT nextval('public.asta_id_creatore_seq'::regclass);
 ?   ALTER TABLE public.asta ALTER COLUMN id_creatore DROP DEFAULT;
       public          postgres    false    215    214            �           2604    17455    asta_inversa id    DEFAULT     w   ALTER TABLE ONLY public.asta_inversa ALTER COLUMN id SET DEFAULT nextval('public.asta_inversa_id_asta_seq'::regclass);
 >   ALTER TABLE public.asta_inversa ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    217            �           2604    17456    asta_ribasso id    DEFAULT     w   ALTER TABLE ONLY public.asta_ribasso ALTER COLUMN id SET DEFAULT nextval('public.asta_ribasso_id_asta_seq'::regclass);
 >   ALTER TABLE public.asta_ribasso ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219            �           2604    17457    asta_silenziosa id    DEFAULT     }   ALTER TABLE ONLY public.asta_silenziosa ALTER COLUMN id SET DEFAULT nextval('public.asta_silenziosa_id_asta_seq'::regclass);
 A   ALTER TABLE public.asta_silenziosa ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    222    221            �           2604    25751    notifica id    DEFAULT     j   ALTER TABLE ONLY public.notifica ALTER COLUMN id SET DEFAULT nextval('public.notifica_id_seq'::regclass);
 :   ALTER TABLE public.notifica ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    230    223            �           2604    17458    offerta id_utente    DEFAULT     v   ALTER TABLE ONLY public.offerta ALTER COLUMN id_utente SET DEFAULT nextval('public.offerta_id_utente_seq'::regclass);
 @   ALTER TABLE public.offerta ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    227    224            �           2604    17459    offerta id_asta    DEFAULT     r   ALTER TABLE ONLY public.offerta ALTER COLUMN id_asta SET DEFAULT nextval('public.offerta_id_asta_seq'::regclass);
 >   ALTER TABLE public.offerta ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    225    224            �           2604    17460 
   offerta id    DEFAULT     h   ALTER TABLE ONLY public.offerta ALTER COLUMN id SET DEFAULT nextval('public.offerta_id_seq'::regclass);
 9   ALTER TABLE public.offerta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    226    224            �           2604    17461 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    229    228            D          0    17419    asta 
   TABLE DATA           Z   COPY public.asta (id, id_creatore, nome, descrizione, categoria, foto, stato) FROM stdin;
    public          postgres    false    214   �f       G          0    17426    asta_inversa 
   TABLE DATA           <   COPY public.asta_inversa (id, prezzo, scadenza) FROM stdin;
    public          postgres    false    217   Ͷ      I          0    17430    asta_ribasso 
   TABLE DATA           ]   COPY public.asta_ribasso (id, prezzo, timer, decremento, minimo, timer_iniziale) FROM stdin;
    public          postgres    false    219   �      K          0    17434    asta_silenziosa 
   TABLE DATA           7   COPY public.asta_silenziosa (id, scadenza) FROM stdin;
    public          postgres    false    221   o�      M          0    17438    notifica 
   TABLE DATA           E   COPY public.notifica (id, id_utente, testo, data, letta) FROM stdin;
    public          postgres    false    223   ��      N          0    17441    offerta 
   TABLE DATA           G   COPY public.offerta (id_utente, id_asta, valore, data, id) FROM stdin;
    public          postgres    false    224   �      R          0    17447    utente 
   TABLE DATA           h   COPY public.utente (id, username, email, password, biografia, sitoweb, paese, avatar, tipo) FROM stdin;
    public          postgres    false    228   ��      e           0    0    asta_id_creatore_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.asta_id_creatore_seq', 1, false);
          public          postgres    false    215            f           0    0    asta_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.asta_id_seq', 56, true);
          public          postgres    false    216            g           0    0    asta_inversa_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_inversa_id_asta_seq', 1, false);
          public          postgres    false    218            h           0    0    asta_ribasso_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_ribasso_id_asta_seq', 1, false);
          public          postgres    false    220            i           0    0    asta_silenziosa_id_asta_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.asta_silenziosa_id_asta_seq', 1, false);
          public          postgres    false    222            j           0    0    notifica_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.notifica_id_seq', 2, true);
          public          postgres    false    230            k           0    0    offerta_id_asta_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.offerta_id_asta_seq', 1, false);
          public          postgres    false    225            l           0    0    offerta_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.offerta_id_seq', 18, true);
          public          postgres    false    226            m           0    0    offerta_id_utente_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.offerta_id_utente_seq', 1, false);
          public          postgres    false    227            n           0    0    utente_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.utente_id_seq', 17, true);
          public          postgres    false    229            �           2606    17463    asta asta_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_pkey;
       public            postgres    false    214            �           2606    17465    notifica notifica_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_pkey;
       public            postgres    false    223            �           2606    17467    offerta offerta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_pkey;
       public            postgres    false    224            �           2606    17469    utente unique_email 
   CONSTRAINT     O   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT unique_email UNIQUE (email);
 =   ALTER TABLE ONLY public.utente DROP CONSTRAINT unique_email;
       public            postgres    false    228            �           2606    17471    utente unique_username 
   CONSTRAINT     U   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT unique_username UNIQUE (username);
 @   ALTER TABLE ONLY public.utente DROP CONSTRAINT unique_username;
       public            postgres    false    228            �           2606    17473    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    228            �           2620    25746    offerta after_insert_offerta    TRIGGER     �   CREATE TRIGGER after_insert_offerta AFTER INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.set_asta_venduta_on_offerta();
 5   DROP TRIGGER after_insert_offerta ON public.offerta;
       public          postgres    false    234    224            �           2620    17549    asta before_insert_asta    TRIGGER     y   CREATE TRIGGER before_insert_asta BEFORE INSERT ON public.asta FOR EACH ROW EXECUTE FUNCTION public.set_default_stato();
 0   DROP TRIGGER before_insert_asta ON public.asta;
       public          postgres    false    233    214            �           2620    17551 '   asta_ribasso before_insert_asta_ribasso    TRIGGER     �   CREATE TRIGGER before_insert_asta_ribasso BEFORE INSERT ON public.asta_ribasso FOR EACH ROW EXECUTE FUNCTION public.set_timer_iniziale();
 @   DROP TRIGGER before_insert_asta_ribasso ON public.asta_ribasso;
       public          postgres    false    219    235            �           2620    17474    offerta blocca_offerte_doppie    TRIGGER     �   CREATE TRIGGER blocca_offerte_doppie BEFORE INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.check_offerta_presente();
 6   DROP TRIGGER blocca_offerte_doppie ON public.offerta;
       public          postgres    false    232    224            �           2620    17475 &   offerta check_offerta_creatore_trigger    TRIGGER     �   CREATE TRIGGER check_offerta_creatore_trigger BEFORE INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.check_offerta_creatore();
 ?   DROP TRIGGER check_offerta_creatore_trigger ON public.offerta;
       public          postgres    false    231    224            �           2620    25749    offerta send_notifica_offerta    TRIGGER     |   CREATE TRIGGER send_notifica_offerta AFTER INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.create_notifica();
 6   DROP TRIGGER send_notifica_offerta ON public.offerta;
       public          postgres    false    236    224            �           2606    17476    asta asta_id_creatore_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_id_creatore_fkey FOREIGN KEY (id_creatore) REFERENCES public.utente(id);
 D   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_id_creatore_fkey;
       public          postgres    false    3240    228    214            �           2606    17515 &   asta_inversa asta_inversa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_inversa
    ADD CONSTRAINT asta_inversa_id_asta_fkey FOREIGN KEY (id) REFERENCES public.asta(id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.asta_inversa DROP CONSTRAINT asta_inversa_id_asta_fkey;
       public          postgres    false    217    3230    214            �           2606    17520 &   asta_ribasso asta_ribasso_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_ribasso
    ADD CONSTRAINT asta_ribasso_id_asta_fkey FOREIGN KEY (id) REFERENCES public.asta(id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.asta_ribasso DROP CONSTRAINT asta_ribasso_id_asta_fkey;
       public          postgres    false    214    3230    219            �           2606    17525 ,   asta_silenziosa asta_silenziosa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_silenziosa
    ADD CONSTRAINT asta_silenziosa_id_asta_fkey FOREIGN KEY (id) REFERENCES public.asta(id) ON DELETE CASCADE;
 V   ALTER TABLE ONLY public.asta_silenziosa DROP CONSTRAINT asta_silenziosa_id_asta_fkey;
       public          postgres    false    214    221    3230            �           2606    17496     notifica notifica_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 J   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_id_utente_fkey;
       public          postgres    false    223    228    3240            �           2606    17501    offerta offerta_id_asta_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 F   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_asta_fkey;
       public          postgres    false    214    3230    224            �           2606    17506    offerta offerta_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 H   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_utente_fkey;
       public          postgres    false    224    3240    228            D      x��I�%G���N�
�6�.(\<
�4\��F6H6��UEi���;�7"ndFD�P]q��H����L��f�GC��?�c��?�����ݿo������������?������a}J���?����?�����������l�~�^?�e}��_�_?>~1q������?�n���}�����;�������������w����?}�����~���κ��~����S�}X>Y����k����O�y��׿��U���/����w������������O���,{/c�5��jH�?�1���?��p�Y��w4х}�?B���(����W�i����9K��9c�zv��W��g���|]���ם�͟{����)��}�����N3p����ᆔ��w���g�������M����h�w10��}m�]������}�������v_?�ґ��s=��x��V���������"ΰMp���J�z×}=�����Z��K��7�K�s�6��y���~�{/�IF�K�2.�x�߹v	�g<��ī������6o�Ӽi�Ҿ����^��w�M(�������]�sm���~SB����q$��84���Hg��	kL�u�<g�o�o��v�Vɝ�]���M�j�o$�Y��_W��>���]o�M֛����yT]~�_%���k4�J���ES��v��V�l��Gd�KylM��Wn�rngXa#�6��S��k}��H�s*����qYӼ.�s)���G��9�#����[Ρ�>$�P.BL�0��kac�Jh���n3��*�f���f�z��f�]�O�ڻy+�o��)�|7��x6oVW:�4q,�� �+�Z��棨q_��s9�aZ���5�)h~�éY�Ⲓ��y`�}�Í��ڍĸ cң�?�%0�7��9	)��GD�e��m̒1I����ܘ�Ȱʋ����x}�>^���������x}���^�&o����iǘ��=��g�4��q��lMOu�DP���}�J���@w��sv��Nx��t�l;k�&��{	���>�wZ;i�o��坴�i���XMݡ��G�iE����v�KL��,������j�H�.�WZ��֗�p�^GMww���\֖�����6؞1�1�-1�f}�ɮ�g>9��jN�Y`�ed���#�Z�Z{��	g�a�{�u(,^D��2�����h6?!��cU�k,����Guigϣ3D-Bg��)�L6����h�qϞ[�5x��u(v���:��_:�}5{u3j!�f$����ۊӱ*D�I�5��ԛ�s���T���9~ZS㭾*�'J�y��Tx�G
���9-����?�+��Q4|��z͵7_�v�k�c3a���r�g���l�#Ѱ�� $q��0Ɣ�]������#sV��r_%l��1��\y#}��0kۯ�\���&��+y�.���"�^��a�P����\��<�������"|�7˴�<K�HJ-i�y���N����݌�k�]fj�o,|Mo���q�9�=�)�1����ɷV����ª�p7.�K�z7#��bN��\A��r܁�.맬w�)�?��Uz�k���Ҩ_�.����J�o��?�y?��c�?��c�?��c��}|-����*�h�ۭ��G׹���b퉿�r���m�?��_�b�8�f�F'�L3�5�V��9n��V�G�1�2k�ks}�4�,d�e�=�-˂��d벥���^�&���LR�F�(����ݢ�+8ѯR�7���In��X�nVgN��ͭ##�6�3OTrd�}��r+2Es���Kfp�6˃��RX�w�k���8�|�Tv����iSR��j.�����kߨ= %0�Ѫk1�8[�.�Vz��t� |�M���~)I����?��FD�9?��٦v}z߾׾�ok���n-'�Ƈ6��{�D�cE���84}��gR�@7s�e�X,q[��Ԑv1(����_B�_�F��jOa�5j�����Ha�hs��oi#|}~�+�a̞�x7��#g]�!��k	��!���ֵ�α�[b^�!�8}q�����ͦJ���FK���0��&��0s�<A�C5[2n�X�yz���X%य़���>��������e��,v�1�lg��g1n�.��6]���[6J�[(O٘�e�_�F�� ׸�0�ѵ���1j��gFY=/�+��/[���B���W%4��v%kVv���"Rs';���G��w_���������hϽ�ڶ��T\_�A��%m�����ɠ/V��s�jY��Ǜ�1p��c ^z���{2�Y�@���V(!'��>�jC��e���q��S^�P���3�h�z������x����| �1R�R0��@ދ�}��b�IJh!�;��*U� 79���!!��(�t7?�?Ol�m�����Ε��J�ء�c}������eG0�M���,ҕ|��^�<3�f�W7�Ȫ�:� ��sq_-�w��aN/o��6I��ځ����ױ@�:un�:O�rD�w����g����]@�3���1n�5��Y���3	ʜ.�6�`r���$+�먎K�}�� bK�����{������Z;����&��Y���ׯ�y�v/߫�U��i	=�F���l�M����׿����e��HdLҴ�&1�`/����D����Od¬�7+'�&�#��K�sVI�n�3�[F�#�����!�Q!``�jIChё���)C�`" ,ap
|�ـKJ7%�6����;	����S�k�m;Z@-lS�Pl)f�D��X�TDow��$ǹ��{�b䲄'��_�4��/�
���X���grCͲ`�<�x���3��^�mҀXY��i�� Y�(�e�;�@_}Bw	r/��w�`Sm����Q�EQ	D��9��P��C�	9Kr���h����K���$��(L�|��A9����ݎ�q�K/�/ ����#�m�4�f�ًuiq�i�\%�63KŲw�^me�=��ݷnF�-l�ⷂJhW�`��p}k����G�8�'��d��ZY�<2q�ߓ�7�p�є�eP�	C���Aڙq񸈢�������n��	>�=P��i�	�9�@�,�j�7
e;��{�eMF~
\{-@���f�W0P �hH�U�A�MLs��!�~nV�����b%t�r�Ў�/�[��$;�6ˡ�<F$��/��b��2�
����U�V��k�7:�9{J�:�8
��s��-��/�Q�.������Z���[ �Y!,p֛yh'
���Ρ ���I
V�8#D���m���bk�V�jJG�lqCbEtRg),�q`��媬�U$���1�mC�Qw�z;��[�"��|X�Yb�����G��w��G���b��c� (F�%��JQU�y��w����!u��U�b���2�
�o<8�]�*`�P�*���2��KЄ��:�}l�so�DFP*'�[tji]Bm)�]Dv�>�ua�*�X5��	� ]23E<���寈4��)�(��}��n���]b�	�[����F�GUvW��F�AKz��޹�yiX�]�����O�N,8�b$܂�*!�D`1CúU�e���i��o���9L�5qg$$@�hr�Lc� t��sx�P2ob���G�m��G̟Ŏ?���M Q�����'f��'/3]\U!�xr
K����;<�w�BlpׄF��m�RuD��+�T֊B�g*��]�Ks��Ηd�o�a�sA-Kd�c�!b�n{@�y&�k��X�L�`��R_G��� lW���� aP,`_7�D��M��ࢌ��� ����=F+�:Q)��f{eA"��5e����@�x*m;��඙?���:6d֦I9����~��1 �hc��:E#L`�v�'�3���4�3L��}5�v����#7�|�+7uo�0���`p�i�Q����`�3��v	�4�4xs&�a�o�F��lG�[\�
�Y��
� ��@;��8�*k5�?pǓ��l���{���Q��l�����c=W������RgiX��D��S'D�p�0�VA,�a��nH�x�I�:=s:���9� �yD���    �5)|Gf����b�:�U�9����T��B�<c�u)��"�x¸�	�M*ϔ�ɘ���P����Y����ۙH�c�?s� ��Dڙ2�zMsVt�-���et3��ϒ�6��J]�T���0��8���q7�O��R�wqW���q� ��"��8�Y���%�/�hz{���D��a�A�o�f��G�O��2�9.p�ǀTo�I4{�/�Pj�p�O��Q�)I�nm��15Lw^�M�8�禼|s ��Φ�ų��8b v���n=�K�J��ca>�ca>�ca>�ca~ӅY�Y��y�a{�ߎܪ��"*o�l]�i>�NQ������ �F�へ����T����#�T`l:W*��P���X�k� ͷ�T�冂S���p�����'Ox�	�rVY�����`����o��<���9&"�B�nm�Ȅ�z,~-��9f�`pl�sU��Լ��7�6u>{#+���K>'"o.iV��L��<��Fμ*´�z8�ɳ��{��D �]���tp��jvW]�Y���(����y�����R[]�i�.�H8迪�텧@ѳ6>�d�sl��+��x���mQ���M��HH���h/	ъ��>�n	���i���3'��6	��G�S�M�t+!�X">D@�\V03ΐ�El5oA�*5�╭C�>f�y0�����nWf|+�Eձ̏_�Z��LO�QY�d��ϴ<��v�e|��PY�&��cV=������;��&mp��A��c>�T̝�z.�����42z�X���ɾ��w����0�}�̺���&�1�D����>g<��^��(�`�K�.���^e�ٍ1�y��2U��CiJ�}Y[��&�vpL\��k*&�%U�Lg5��X�|�j_`��{�>"��<�J�CV~��hW����|f�vi �d�c����,z��U�(�X�~7��	1��]�n��\�_�S1��z�sSީ���̺���)ж��я��k��t���fʾ�j�Y{L��������,�rb*�Fu��^ţ[��X^y�1�1uv�q��qX���{��}Ģ�RЄܺݓ<��Qn�_d�ʤ�sK��������.}�`���.d+{��RZ,�豷��Fp)yS�����|6'�"��6$���d:�mbx@��Ԙzɱ2�� �w���s��V�!d��Va��� �k�����n�G�����ãJ|��B�Ncg�C��"�;�{J��:�d�1*�b���@�Q�b��5Fl|և/N�6���s�ڦ�f��ZSY)|���c�o�@��<�y�>�Z�a�h�~���A<t�%%���E�u��@���E4�FI(5*��Ҧ���|�m,<2� �M��,^�m��܅E'WN��ET�Rڛ�*`�Q)��A!���zl��M?���g��UT���ct��8�!�x�ܔN�N���D�] Z�{:��u`�r�����K$�����̻ ��m��ٙw��b��`tлH��3�� s�?��(�("�%e ��)�T!O��,�9�	ݺ���	�j� � <��O\� ����D��Rw̉}W�++Y��+���� ���Ц�^L�g�V�
��{ԓ�#�EL�"�0�߈��։3%b"�#<oy�b���j�b0a�!��:����	y�A�tJr�iD�Z��O<e�,��@�%�@n,n]�h"mA��������yd�1���!ʫ�t}w H�T5\f��LDer�9[�9�{���Z�vA��o��a�9�c��`	� �b�L"�7�>D�������ěl[���Z*K�u�����	�OX�|!�XeJ2]��Dq.gAp]���Er%"'MQ�$ �6(�h*��S�>��H#�$�����$�f�b)Ker}k�>m��n�Ll�
�n'�H(�t,2��E��"�*�&�J���Z�_�J�)`��z2jT8
3,����ADK��/A��|�	���	/�Ձ_OK�}� �H��1F���H�*��ы"�Ǥ�eX1yS������B3PMm	^G�J����۳5��I�Ƈ5�V� �_����T���
E���GN\aYr��`�V�\Ձ>�q����c)�ać���R�A���Siɣ��W����u����|���0σ�ڜ'v�*�k�LD�WE���#VE,s��gf5� �xC�أφ�&�s�` Tk*#os�T�����C���N+�$0��F^��n����)ϓ���O9��ʚB��r{>��Kr������6/��a0'j�˺+�O�3��H���(@���2�����,*�0;)����Ry�K�BY��Pa�-i]N�[_�H>�-�А��A_�)�bL_n�;�|'.͗7� ?�:���� �]Z>�%%�U�cΤ���
�b�d��S]�,J�|��
=�l��b�N��Ta*߰k�ed�67�[�d`��;��BEu��xA|A�;�H a�
����s4Ai��^ �(�& b~o���,���!8��� �16�6�T��(����޹�"w �9�x%j�=�hw�5��e)~ED�	k~�5��iN�#th&�c�k�`�w�Z9'�Ft��2���r��kc*%ρ�d��ѐ���#M�dYG�;C&�!8E�FS�x�q�j��ѧ�gr'c�0���rI�2�Y	fLy�K�^Abm^*k���Q	�c��%�О$�߹C`��E���� K�׫˷���5��Ъ�bY�{��;���Nf����q�s��|����1��0��a�g��]ՓL�B����o����n4�PA�������j�0��T��0W��ߔ�����:�mYlLz(F� �I
x@�iĹe�y�J���R{���Y'qh+�i.U���7��������'a��ʹԖ�N�_k����'�%l�1i�,��aF���}�i�N��밆�v��u��T,�%o]1�?��V<���b�^Y�h8uE]_=�����z�E��w�l7v�5Ά�0)R������C_la�����yj� ���V� �k��W�U�"����������P�+��ꌙO���6�g�8��!�WT� 8� =3J�:�zꜗ{%�R'���:����;����-�I��DoIl=���k�t3��	��cgNtp\#Q>�]�C�uL�/�<�ۆ&��t\�te�c����1���1����<��l>>��o|?�7�?&B�~�m�ՠt &ಖ�2}�C���5�g�g+5!��.޼��1�3f1.q��:#<'1%g3�C����?'��;��ͫzغ��v'�"�ل�W�b�!v�V\9���ek#��S�"�J�wv��Ls=D:UB�e��+�e5	���N�j't��x��R&��gߣ�!��g�m�>�縒ؘ�VM}9���\�h@"kYۢ�7�:��5c�����z_|�=�y�"o��o�p���oj/?-��I��p���V����x��0	C��vF�e�YZ����[�[��$?���քs�c����M�;��*zE�z����YTg�M�5��-�(�椃��������5>��g���aE��0��p ��n����g��dy�[W÷��ic�GZ����[y�J�S��y�c��(I]`��\Fk�~숗ű�x�U.�+����3�~*X墀O�$ޛ6��_�{�������e�+N��yo>��GR}�g�=���䍉>9{�R���r��vYщ�S����������� ��>�����\�)x���l>�(�Hm�й�,J�7Ǻ�6_�J���>Ŵ+2�v���YdeyYs�sVVN ԩ� \�S������ע)��t+���j��Y�`�fw͌v�K;)���S����2�ȳ�<[VJ3d����[M��(o��6n� esO�y<�nYb��m���8��d�4��c�(]Pj���q���@rґ%cU�_j/�z�!<t��D��E���C�v�Z.aB�[�Tk��{q�Wx"Üj�牚u���V�aD��&�c�y1�`���C9CV��    )���{�v���~*�'��M���-p�vI��:��ZV��{�\���sȋI��gC���*C��5$,��o*ޣ�|=�R6?�~�"wq�j��:K讀��z�T��R*�� ",�z�K��7Qℌ��U���l3������~��U,"���VԏK%��=T?]�m�Nl�"R�B fbL�C��"�*Ds�;n��+ D^�w���E��aؕ랒�1��9A���nS=�.���A�8�Z�����.%�1EXV�u��W�;�}zf��s�(N_<�#q���d�ٷ�L��d�%DM~.6���݁X6���9�`ZR�D���CD�-1�Un)��j��ؿ�z";	�b/�"�kՅ�o2��?�.��Q]�X�As�E��(#��}<Dǈ;�"<�C���f�F�	\e�E&��b+�|˹���+�vu�u��T�R���s���\���&&π7 �Ռ��*�*ba����`�,�W�ɸw���H���R.a��,leY �J��|�a��K���pvDe:��UK����ڗ�
4@3CZbG\����d�w`}�������	�\�-�>,cU�ΝH�[>�+������"x�;v	|��0[�T��:H�&nU8�V�N��Q^��:�z��Ֆ�bR��+!Jv�/w9J��1�F�)[n�1Gq@��Pp�?�u�	$�{�go�����"�S�l�����E����/~�"w� ��JM�_Y(h�V�p8z-�'��
�h��fv�� w
댝�c���$V<+�&�9קW<���n$���4L��|�.J�y"_UZV� J%r�SE�b��x��70Oi�F��Jy�s&Σ����:Y�jb8��͟�\ �=�9K Z���[F@VU�5�*�l���/��t���w#؎�3!L �]+�K�|c��%�W5�T%7cq��	�E=��K��%%O�8��Y([�>�I��'U���I���+\m7�:\��� p͕�&�j���.�2*+ �!����C�#�.⻼�����r�.��N��ALh��D���E�)a����S�^��zW[����S!/:�X��*Z��"�]R���~t�� �����/���|YY��R*��;�PGV�Du)UR����\�� �+�I�E�@�1��|�� �z��Yp��$v�#�+eE�4Du?%��f |:����Xx,d�m(���E��F��m�ʣ2jm(����:��m�]f$^%�ƨ`��Z��d��X�@/W��!e0 Pm��&�,��d� Z�T�����,S엊N��Sl�ͩ��h�����)W�F�����o��)$�;h��"cGk����}r��7I��(*w�I^9~��*;��A5��uc�X�IX�U����Y'ȹ��xGDͩ2I�>��e�$�tYldh�9;��\9ք�O]u2"uc�v���>0� sѕ9�C�*96xߌ]S6�N��*v��7���̒X&Ԅ�C���I�̄��Ұ"�L��4���\�ʢ��ܑ{��{���k�|Y�1K�D�W�|X�_z|��>�a+('9q=�.[��g��3��k����K���gV�qF|�<���22��H�_���?�[�Y��׍��I�y��i"�Ë�����eB@r�B�N51�d��V��.�MQQ�U�r�ы�����!�y�C-��c!p�j��H;_�K@�\��^�����E/�� q��k;���X19�Ӓ�X���z���弹/9�SP���*�&n�Xu-Fy�,�?{ʒ�M+ۅ�L[4~	��U]�J�so,:��)'>�v(�{V
;~�nՖi;ٱL ���Vy��;F,�w`�����1᭙_��U�ẁ�6�� �ؔW���ri	X�n?-������a:�W��6d�T�&��S�S�U�O?V )@B^�?� i)�Q�Q�ʥ� P�A��bzP]�����+]NGP�j�}�k�.�s$'OM�:�&
�(=b%����n5TZ��T�� @�M,�U}��aŝ���J��s��ĉ:SI^T)�3ŋ+0Y8�L\K��ʚK Έɯ�y�6F�C)tʯK'�����V��1z���N����W�|������
K�v1�8�n�+��a՘��iN~�0���,����1G�﬌B����^�
��ئ�d
K��]���ب��Q�$��u�C7��|�9���PL��,ç�,��xG���|W��HN�����c�7�:!DB0�X)L^R<�-`곋�p����too3vJNZE��`G-܆�+��n�d�v�YD
�����Y��f�dobҊ&D���R���"� �E��/%9B��8#��{@�I�"���LSp*�f:�ĿNm��$��1�!�֡�82����O���Ffq��^w8A��l!���oȒ)���'�g_?#����M2B��qYH�Uc�|�U*�ZV�B�߀|��X1K��a	��&Jp��k��%��]q�\��]����hL:9����@@J�����4�H�9ϔ�/��1 5�eӜpg*_㻐m!�:ŷ�1��������gn.w����"��.��{��1�Y���[WL����ъ}u=z��g'�E0�T^��I�5w�ۍO(8&dd+��[\��JC.��S݂��Ge���wܳ8%�3�V;E���<�r���'n��z7�$s�� eg�F�-���V�R]P>&!��4���%��v|0W�H��s���ez�t��d���M�XA����8D�SO=4~� Do�"��>��m��i} ��C:SO���Q�^����M�m�ng���ӈ��L�IY]F�F�6^�^6���^��Cz��G�u��>0�DP�ս��<K��v7�j���F���
5�R��Z哝ή:1 ��d~%��a���M̥S�"~�"��r�o�Z�Y����`%�L:ǅ�T���œ�5Ƹ��������#�ڑ.����p��'틜�p�r)[�w���T}�aP�<"lh*ψ�/q;y���p�I+�Ր�����M\hSE�N�<�s���|�~������,H�ͫέ1Ȥ(bT��?WB�&6c9���Pt*�Гhލ��>	,���ġ� z��J5"�`���;�&�-г�Ñ��Ĺ�ڏ�v��O:�6��>���O�<���J�� ���A��byA�F�"��F��wu�:��̭s��)�G�:�yP9�9�8�Ŭa=�p�;\���a������N���@e�8�&�h��&��u�6TB�� 7�7���qU̠�	��8�X��q���J�E��P"ǒ�����9	������3�Q<U�c�*j�l�42>���V��Y�	����kw��S�4�g=�v��:���t����RB�� ��}Tw�X|R����"�N.�/���Z����`Ip�x�ve㠃�Q��#�����FEL��ۆ��uy�i�MD�@�tT��aUA��h���H6�B&��r���H��]���]�t����"�Jh�a��ֳ�T�N��&t|�"k�@��l�+Q)������![�YN�S��"v�W>:e���*��qħĭ:�^{���i,����h	xXCџp q����(_�X��ԗ���"S�x+_5�[g���7d�E��mT�dЏ��W�Y�����j��y]P��&��*��UGA 3(B�!jr���[�,sY�w���hߩ*b���j�,NE$��`D��l4�'�,�W&�4�0 ��$P3Ƿ�c%��/����t���7���<Y��ff�(�8�SNS �<�f��FT騽�y��3
V%M�#&?Ei�'���#%3��b��<E�C��/���}�%���(KH��ABF[xC��F]��)@�1�y_rPR�G%�݉�|i��T� �pS��>�̭�/�s��N�c��НT�H��A5��:5"tx�є'�o�'!c��Q_�Ġ'5��H�hs\V��F�JV"{6����	�,
P��wDP��~�Jj�?�    RI>�3,�}��v
���V�����y�=�u���g��^��⴬���ٕ=�	��++W�78OQ�%oHF;���d����o@��4����`�s����bp���K��b�d���f�ba��jJ��#�2�(��_���5\6�/y��@P�ؖ�1�ky���,�*�Q����7{G��[걱3a�| +�N��D�(��Ŧ��gg\C<]��	T������V�0�Dsd�g��g7� _Frj��2���S{�;�<�� �����:��whalO���X�pk����>\o�V�@��/�Us�dw|�9�&�t@J(�']~�vm%J����9�R��k��|���i���ĭ��j� �R�o��p%FH�H�j5	������+!C����}���[����|�/�C;����e%��rC�"��뚹Zt��G��������7��k�!�+"."��֕��,�LiN�2��r���#��?V�"��6�Y<��L`\�E�]}L����/�-��%o-ˠ�0�k�ްfB��Z��[�.qĤd�W>�I��L��Y�w��5�-��uԆ�O���#\���Q�*&����.
I���7�'f��t�9�_����.ʯ����,�-|-���Q���`15��H.�a�A߬9�/A3qн��rG����e���5�l�8x�S�Zm�/��;��u���>�eQ�� �$N:	a�X�|��"�hD���7�S/FDWՍZ��D�z.�""���U	lO� �w۪���]UGԴ��=���8ē5�'�Ja�F���o^���pH��}�"��G�J���&�:չ�N{����R:@�Ц���2F�`!�Τ��&����h8=�B�x@Y�'ק�d7i+\ٗ���[:1�Q�(]��;[�v�����{�R������AjH�W1�%n���V�j�FP�$�-l#n`�&�R��E��:�P7H+_Vm �%���� !��f�����VKS�0%6o��W4״���Vl�
�0����d���]�$��(:O���dtT-1������㪗�W9��VI�*a�2Y�Q�pn'����~���G1�.e��? 2��B1D[���]`�g�� 0,��w<�dk�*��+y��Y|8�o�,��⟹u��C:�?���e�"4��ۭ8���j핑�'Dp��S�fA��C�i���PHgv����vU1�����:�*���.���0hEp[��%'&��Ћx�SU4����v�LճK�Ț_�����Գ��3!ڄ��Ӵ�����"_�y����j�-��S9֢B����V4��H�����4AJG\��z�8�L�z�q�����x-B��D!-��$9���R�:��xQ��`�gMt�*��'��R2[L�1c^�+Q���+��_ۑ5�e�SG�H<�x��ӺzzG||S����>k��*�Mxc�@��OA��Au��I<{�wL���/��ݰ�Ww-���
уZ�3��gν{�FʚQebQF@�=��ܴW7,�6$��#����[;�mڻ��}v$j��}=�^��orք0��cc0��&�jcL9[�
�Dm��RKh�Ӳ�ik���cSP.Vb�����e`���8��Uf��a,2��7��>�n~�}�j}��YL�rp��sO��	��\k�@)��3O�>�n*�$Y�'��8��������C����2C��e�`�DIґ�'@�3��'T��x�<�}X��1�3, ��7?���=X����*������}\�\�G��<��E�.0��U<��T�۔����*鐦b��r��|V���P�Qʺ#�D�� ���2�|�ڠy�)�T>n���5]R?����p��NN7!G�NuW��F�7�(|���F�"��T��kAh�"��Ifƴ�������n�9gq��YV�>���� �&��Q����b��9�:��*�DC���<���4��˻�>����W����vqt@UA����ܰgq0�hef���M��f�����"^ܢ�ݪ`�U��!�$�|mx��'�����ؖ��(�,��O�	����X6��i��i��W��$��'ΫFLI$��t�������0�|2���c��Tm��J{UF���0#�nD��RK/Jף򧓳"]wQ�2�l��l�h�K�[tPrkn������U�P���94�GƢ3�h?!.m�����8vD0Wmj�:#&Aƨk�NDIEe�u��Y�M�iB�V��zeyc��9k��+)swg2Fo+����
��h�o�DP����VF4�jB��\7.��!�(ڎ��>������q���w6��!!iM0��F�,��Nv�2D�`W�z�`���G�"��8^b���=A �����2F�`+l �jM��0��*�hŠ����o��V��J��^P���*�9���F�����F��p��w������j�Sd�~�i�A̰�hMv\e��ʶ��Z��T�y��8�Z�l�][!x�`�Փ�/��l4�\�/�d��,l�8�R� �2oD�E������,E)^�%��&Ak��17�C�2ӊ0ij���,2\q V����Ay�,\��F����&�:J�E���w���lD�����خu�Ѭ�����\��-�x��3
ފ���)���&e�������z���/�q�f��s���2U��pE�D ��
���b`b��0�3��ffb��F��Q��4=}����^�SŐ*Nhj���R��}������W﩮��j�6�`ZܓH�v�]�Z�N��y �ڭ�9����N̦B���\%^^��CV�-s��:9ZAaz��FE�B/A9�D��K�Ε(��������%v�C_��<Cg�W:CS���:��K��)�\{3F�Q�?�-��v�F�0^�%>����zck�ʫ]�QyC`6�H?�`�(o�C��თWV�����dUI��UI�T��Z����Os�Hda��j5u[�-�j�:0�X���+�JŸ8��b�#�]P*��$�C5��E=�P&(�~
<a@�Z\��^L$&+Kt�5�ջ*�U@�͌b�ӿ���f�#�O��Vr����Z|��*��Ij�:"H0Z|-�Ә�{6f������f���)Ġ#�r� .j��D�Qٌ�eU��r���� �&A���?DC�B��nU�)�O�혃��6�o�{H?=Eq+VMj��
	U��
 O�L�(��v�'�`�E���I�̜�pa�S)9R!e�s�Q�?���A'�7�WK�3���}���<���݁ł4�D2��AfM�^�h�|���3%s�\@��e�U���q�Ac����/U��r]]�m���rKmQuo���HY�����H�U��Žrg>|г!�*u:�h'������vq��S/��W`��Þ�}[�ZQ~͊����Jc�*[���@I�ө����] ��!F��)]�7Y��2O�C�LU�D����戯kS���mdD�P��b�j\ӑ�#�N\i����v�!%�F�����Y�����kY���vu�!>K+]UЋ��і���U<Z���t�9�z��|�VJ@u�9H@{���U���4`��~^m�%�V�F�$\'˩蔗���xT�M�X��:��j�l�R�?B�
lU��J�r�1�����W֒/.B���Վ�����;������{l2V�#�YQ����ܵs��:�-���%w�u]�N���l��V�����X}��f�%V�)X"�2�l�	pm0`�ا����^s���ڟHby@��x:�=mcOLˑOH{(@�דC(i;���YeP��G�GM�4jO���W=oyOJ��/��+����\7㥛D꠺j�j��X� 
h*nL�����u�����go�I���cB1�@��N��A1�|���`�P�����U�=��8�XӋ� �6����zF��v/�m90� +��wJ,_����S�[�Q1����iF^ێ*j^}�Q��V�5np���o�|���b����JqEI�ț�D����4��    d^Y[�xP����X\\�
�թǙ:��K�Z#�;eD�2��d��Y��EG+W,a��w��wox��w��x�"Z��r�-����\P���/3�Y���j�2�Q���4�f�����7^���d�u8&���I0�p(�$l֕���ӕ�����b�t������@�q��N���GӞ��s�S���X)�Y	5kRYd�u�����Y�ϱzi�s��0پzu	n "��<�̕,hbq��Թ�E�2�(VD��Z��	T�mm�N��a�Dd�6T��&��9BWF��2��3Z�hՆ�����X2��w��D���j��!�Y��B$v��PU&B���#�K*Q�-��d��2�.�=�����^��=&�p�v��ű 67�!� p�l'+��4���<F����C�P ҉�n0d�K����O�#�����|����@J�e� �l@Պls.�t#ڡ�#��mlq:�t�p��ܓ	'��\d��"4�[o�*�T��I��n:��P��H��Ҿ/���#<����/W�>��]�(���$E���S"�h����1���%b�#clƎ:4������ ��3�U�R+�ߙ��a�p�I\9H?���
��
biȢa��u1��j$�iZF�T�w�V������5�A��Î�κ>	�3��C�X�Nv��-�k��h��Lg���unX�E���V;�Uq։�b�����"Sն>~�]�d�����ﱨ�!�m�G^YE":� ʪ�L��U���P&j�� ���~���s�(ZT/oL��ӡb���fW~舣Ɖ��z3F�4/�z+��K�N�w�щ�y/��N*�N)�M�2�K��|圹�8����T�%�UYܩ]�w��$4���:"�V����쾉��DW���p��ؚ����>B�<�{յ��8O��M'2�Z
��K�Pѩ!qRP����젢y�q��w��|���ި����_tD�U���%��'�5�8��/| ��U�2e��T�JC�ݳ������~3���C���|��ވ�W��U��t��Ԩ�i�^�F�P��t�#�_�������wV�ɜ����γ3�6�̋(?���j`d[O����S��,�HB�J�۸bQz����в��́M��S*�9�Qj{�$c�j�@� r͵�K��-}���]�6tr$���Q,�ab��֡�vq����/����6�!y5dv:�?#�����б5���n��"���^ <�� P�8OհK�����ho�0aW���6��T�=~Z^,n6�p�)N~3	:� �AV�	Ƴ]��1�LM�>�U�z��5d��U|���3.�;��į[���	x:��??�W�U�`u.�yJ�f�ұS����iJ���ik��߉���L���4q���bD�!>��%7���JA~�zbC���ο��hK�o�g��W��$����3�L�b��
V9"�q�tJ[ I�<���&*{cB�t�et���8}��@�����;a���1�hޓ1��e�:Q�t�e�7�_��� �^��d�i-��Z�}@Z}�iNq(�҈�yY�Q�����t��D��#�n�F��I��SM�wF�Ql��$�r��D�����U��<�C}G�2K��[�Mͽ��.�)HWMꗀ������ż�"�ŉQoY��m�m�u?�{��a#=M���W�2	O�e=� N��ʦ�}�fp�����ROc�=�(=޺�����+�J�}U+�)�|�(2!�{w�sk0oE]�00���,�/��HR���CЮ�j�_�j�C�N�و]�r�F �W�2[N<ZN8/J�V ��E���7$����G"��b?�{��v�5���r��zP9��/~g�H|�E^Y�:
�UI�I��=�CX0�����N|.j�y5�@;�
ÝRC=�r�ԂId�d���aWCW�`֬㦺�Q�:�D�"[�PQ�dL�`Z��4��aX�{�]��F�� 7���4M���0[�-~�:T׭'�h��&)�'\�xХ����,^��+ɪ�_[�iy�볾�%��ժ	k�\Ŏ-gᦚ���UIp�*V�zN�UW�W�}��'��=�jb��RwB]u�;�&����:�ףS�|���{)��EA�G����S�;E���Q�@w�����;���=�b �&u�yV���Um��𷛏�����_[>9�1;Z&�KLm�8l�6*�S�1U��Y	��l�wQ)��Z�u��o�����L�bd�޼�z�������ھ*�����NTmsd�7���y�o0�wO�DN���՘q�(B�*/�m+(A�Zg4��'Sf�&_b���ܔ�sY���<�u�I���@��dclE9�Y5��lX��}���n�"�*��j�X�[W�U� ��ڔȘ����!("���&
��"\I(�j�գX�aT���ڌU�/�k��zf]�(�W����[��*��"�HG�NE/���|5Q����[k�?P����=�k)5q��GE�F4��09��1���#D&Zi�-s��k�Y�tR඼oE�[�&S0�`����(D���-�S��ȸ����Qa�Aժ�J5*��H��ԓ���^r��l8 1���<���U�)�o�^�V>���r7J���܉Hjq��3�Y?ى����-�D��R��"�[�p^[�d�fƕ�׹�	��'>�]l_�y��,�p�P(G01`7UWy���7���Ua�t�`O�趹�L��7���ס&kJ<9�,��5u	1���Ϯ����y-�n�w�i?��m�_횰�,���V��V��絛P%�wN-;��f�C(�g�7���s�q��9ɲ,��$<��.��U���7v�e�aFQ���6>�����	�̪�"�UQ�O��_�""Vy�����G/�yG�V���\����\-�v5�/
�~��{�N"�*�ׄ\u�b���u1
��9Y��j�ʹ��5�V�N ���n����Q��F�"O�RL�u��j*Ъ�:j��?�2y��aDG�<���X���;�*u�O�߄���O�������§?��_�������NKR�!���*����w�
h�\��;���>����wN#\{�i���p���wF]-�]�4v��������Գ�5^nz���|���K��b�Tg7�	�4�ɯQ>l[�������}ߏ��E��-��>�[]?0��}�)qݷ(����_��ב�����������)��k�$�����s(C����ϰ������K���j^�����(���l/}υ�&�Ҧ�G���$'�"��W����J\�u~Gv� ��|^�h�Vԟ6o�Ӽi�Ҿ�s
�͉���sƿ[�p�z�+p4'4>����������H~�qh$gB��pD�ј0���8�ox{}K��m��|&ޮ���q�p�����0���Ҿ�@�������o^����\%r��`�M�|S�Ȣ-�1��}�+O��;��u�*��(��ד���<2�P��Ovr�$���J(ѫ�Э��LWa�Нr�s*�y٫deF�TV'��NiM[}��9Z�Yy������0�o���ش��a������?�G:��99��+Z{ͳ�>L�1�S٦�V�Q�f)�3?��3G^햲=��xwV�T}q�s&Sա�Mf�	���SFd�1\��O��9�MX��>�i�+\j�9pu*�7<��0�m"o��� Ω���?n� ��2�Q	*��m�Z_Y��V �,�W�!��0�:��2F�ɫ�����Y�_�YO�&%������p���"��QOhU��%��y�A�r�3��ʍL#��!ձ"J�|tDfO_[�ǳ��W%Ī���gXS��F�K��\'8���_�^���UKr��
�)=�ڍ�&p��E���Չ|���~��U΄�����矡"j=a�ScZ��REX�}[�z��VsNS�sE��mz%5�at�qH0�)�.�)����`�-!蚙��u潜�c;	�IܓV���1{Ȋ_u�;D�E�1"I��*�(,�D(=F,�7F7��������1&�[^c+�4����    *�j(]����|���!ڧE���c�JM�[��k�� @���:B�Bpb�PCM����U��,S���I��V�@�����}�>�iG͈HC5�F�y�1}:g���;V3�S6�*CE�֯�Q(����f�qU�����ZȪ!�*�bP۾^<FE���63���X��y���fM��[���v��3�+�lU��T�F/�5w��u~���b�Ō:�r�t��01��-2�X]�u��@=]����Ny��JS4&�Os$~L��Z�H�&����U/� ��4u�D7��6���{FqbUU6�jԹ��eL�꒷�\PgO��j��Tƺ��j��MudN��:�˷A,'���[�4pO,aL�B�ݩm٢J��ˠ�Ū2��#�e�p����9���2 Y�̈́��z��{j��d��F��	�Ab���m�����rO�J}�K[�w�����iXtª�4HY���yzD�x,\̼��ۧ�pWK�5��"��95�n�I����rƭ��C�h�O{�_K�A��OB�Z�v�l� W�Is�z-O���\'
�Ek-��-5��QU`-1cĕLׁ�ˌ?h�U=^"ʤv�A�����h����u����P�ٳ�����b�йnL����4�b�Z��8�Z\�`� �pa%t�$C���8����R����*j���++ޤ.cfgO�t��4ǆ�0�K�Z)�N['��8�n�rU\�ҡ����?�P���Т��Z�^A��:��*֐@� ��0pw�S���� 4�x��̺��C�z�2M��Q9�>��n��ײ:XǪkvW�ݥ�U��؝ȡ0�X �
XFpd��iwq��p?	�,7HeՃ�)fV(���rߴڻ����E�2����WcDAŎb���C��ڤH�r>�*m��CMH�J\�<5�^.i?N�Y�������y)�hc�ﱠ΁���E��q��We	P�
M��A�9^fK�xo����9[�z2�;���c����|������1��^pڃ <u��-cT�jt"`u"�։0Z��V�#� �F��ֈ�0�MqSe^/ �ë)�k�X��S��`H�0REZ�@����E����+L�߱J�J2�U7)�Ƽ���QO�Ґ~��f�rw�`�:W)j3�v[uVn�|mC�����
"�����z����Ř, ��Gb�f�n�]�~���K	��^�:|U���v��w����m�5ъ�救!�� u�r�@&v%�=u���5���N	�y�=D#=[���V���tj$('� '��%��6ةD�� D�(7Q������\��tSx̔9�������A��Vyd��x
�R��T\$z�@n�Y�"�G��܊���"ǍT1=-e��<P�����=�M� L�읖hJ8�����@j��j ~SF���'��2�Rpu*�QR!��E��pC!�F=5���թ`!B��K+:^5/M�轈_S����x�iⷬ�%7��61�c\#���&��.j�j�
+o�(��b���OK�*��Ml����Zr��ޏ�,l>�;=7-�7�Z�8^,�8W�TPCj��l-M��&�yi㦩�u#����-D��fD�����3dee����\bM����B1�� T@ SMG�ڷwu, H���9�u|���u�����:Sᚋ�:b"���&����͒�Ӈr.Z��9&ߜ��q_N���8����e�KLX�����ʥ��I�`�o���F�G,���U=ѡ ������,�D`��Y���6q'!]��^���$���:���t��j��$)��X�`�/�MTiHF���Y�ӌ5%lr�@$4$��齲O N���	��p0�,���h���X��꬐R	�8��y�:�NG$|ɪ���8�0ԴT�o�~�Ws	��-��-~�W�a�)&���F��h��ˢ9���Y�֊#��{�"���5M��6u��V�VC��=�7D��n�J6��cb:�]g�(:�M_7�����1�-��j��9Uq�CoG6S�ڽ_�Κ�D�?�bf��z�M��t"�	FE`��ZY��Ӌn`�]�RcCN�����uea��r����;�)ɧ����A�Ǖ,�DR"E-���/a΁�=�Gf�t���7߼����H�{%0 3�u2��*\؋9vȻ��2j��6�ٱN �Iڮu�%�a,5&o��R��|�t�ɺS^�
WV�c>$��s��R�EA�ī"ھk�׽;J�]�L��r
ntS�:����dH�#@}��3�_W����T
�^<�Zq�E�Zo�j�پ�痽�HpW>e��{ֆ:ќ8�/�g �Tx|���9���jU��Vȕ�	��ǲ�@���P�d��	6�yg����h�O1ojWtq�8m��A$�<�0�|KA~�E�t?�)���70L�Ix������s[nr�+r����{ψ	�	]�'���GΒ�`���/�H��:���L<�
"�K	]���ԕ����z�H���2)��,��ɾ���^�7�eD�O�G�fIr~�|�z�;/��H��M��\�ִ��Re:���J�Dx�I�z(u~�'��i穹��e�����2�۴�"3�T
F��iH�Yw��sm�=6���;G���/�U�G5 ���8����u͘A���1,�g�=��,���ͮЇ"�,"��"]�<�n��$�z�n��Z>qX���MlI��q_�7��AP7P��J��E��B�&�W�ᛣC�֔cjO+e�c�.���, �b�;_.)Ds��o���X�J�ZwG���ְWnA�AŚ�(d�KK�<uZ�w�upt8�N��f�qV%���#��;���2a�)�8�I�o��rD۽�A�����u��M����g,r&��39.-\!IG�Ȼ3��\�X���Q!T�&�Peѡ/��k��H#����B��;�T� 	Iy��b�ds3�Dzjƥ����IY���?Κ��߄��w����S�v����%tE���G?����
o���}�����ߡ���W�j���&�/E�9���O���b'��= >њ�m�h}s8��y ���rp`u��M���>r�6���M��C� �=������S���{3��J;t����0�zk�m��)a:�{���7�nj~�X.��G|>ŕf��i7���i�YSq>�j�����
_�A���"��M�}���@�傍��|�E�r������d��Rv�H,�kY��*ΐ� R��X<,���4E�}�O:�JL��1�&�ݍL2:�J߼����d�~�Mv�KZ�	���$JE@����>���q�ހ����Mh���@��穙�f~o^i�4Gf��`�z���c�k��k�r��3�9�����3)\�l��+z�yR�$���c*}�`�=���8�K?"�q��p*��|�d�z�Q��j�d8F8
��8<���;�BJz�������:Rs��'��9%e]���"�
�/�|�4*�*�O\%��jr%�O#�1s0941N6Nm�C�WԈ�9S{�g��/��د�jë,��|.���p����Z�Nt6OV� �c�x��3�ޖS� b&�0��&O�:
�D(��tk����L<v>��&EZ�TW���ݍ}@�!�P��Db�tQ�LH�\�\ekYb�д^ݩ�]�t8ں�F;/�PIfM���T�RU���b���}t���ۭ.����Rx��\�(ж��q ���쟌����N{D������A�肿M��̙T��c��ῶD�S�ݵ�)'�g�E�����&�S긡��3b�^W�=�B<L�6П�D]�ǼԸ�԰��ܞ�C˦k���8�^�L�RP���c`���6��}T��su���O�e߶4��������*�iK8�ja�O}�2 ' qf�R��ڕ�r{����9QR�ifCv�������1�aw1W�S��?��uRš1�]A*�8�yNk�!ȼM"�"��=us�$�� ^�i�=�Y8�q�%�aj,�<���y��1�v��Íӡ�|Ε�Y����$	W���Lh��ˍ    TJw�[�����e�D��_ĕ�(*'	<���=�Cݵ:؄��lxA�fǖ�����߫�1�R���Ⱥ]ы�PD��y���������!#?����bzǩo9%��vǻN�	R�sB��
M��d���ٱ]��ߎ�'QZE�[������m�*]���En#�Wu� *F���n�P֥NY�������cPT;wJ'�b�n��А�����4��(��4��]���~�m� 89lMA���l�P��N�9ŷ�=Y�q�Ŷb��8�XXP�3��zA����� �� -�UOq�_1S
μ�\�$ ���aW��Y]_cH8��K�g�Y!3C�`o�M�k�+�%�K�Tx�"Xa�%�C�RQ�����*��H�gI�9��H��Oo��9H�UH~S˃P��>�؛3�Y]�T�R�$���܇������D�D$z,��-. :�~�I���S{�e��*	 ڳX�$;'B�� �UuRį������ƹ��IIg"G��d�M7��٥rOIܨ$���=�
v]#�!5�j�F)��բ�/�����&��F���?�����vʼ�xO��d��VW��r~��U��n�Pߞta�����
{��� C7�^��5���hZ��E�M
=]��������뙏F�o,�h����HI��((�E緞�g�_z�B[һ�c�T!��u���}o8[Ɂ�vU�
T��ܝi��U��,��y�݉{Zk���$х��c>��6����2�Э �as���JIAJhw���P��C��=�N�_���D�<�30����K��� �*�ģk`  ���ś ����SNn�����ſRlϠד���cn_�n��9qP�0������c���BhV_��r=�)�bEL�R@�`��;��u
y���Y��/u�4
n(�`fp I�"p�� �~�� �ZH�j��˹�zE�H��V���/�wJ\xln���;ӊXeX�p�'�2{0*�T��W+���7��4.�����IŷJL.uEpn�5�n���ƔNTzMv���!sD	/nАgB�R�d�������f���9���Vx���J����LB:�7�l���L2ՙ��HNΡ�'�Z��
���M �Tbã�U
��KkҀ�~�X�uZŝԓ��(�A�~w����Y_4S�����m��kxr�/�'�����
к���]�i
_��i�!ɏ#����:ѱ��ynj^9�t�[�T͂(�)�(���ޱ�K�y��-O'�;an�ｔ���K����~'aX���X�.��n���ߴȋ$2���;��)j�)p|\�՞�B��G_���T��d)����,�<]�{��a����������_�i��)9J^3���zL�)�wi!�R����ݐi��XȒ���v\�_�ש!Q�U|����&uR�5�V+�4 �[��ډI�O��K�4��)�=D�G����o�+=r�Ny�g�KqLt�J�?���9��ғ:خ�jMn��5�$[�=ˎ��q�3�}��n��r�}7��|V�I3qPqM�7�d�є�"�]Ɣ���	M��6-kN�[�܇BjH��bCc����2=]����8��-��\밗�X��*ئ�r��O�p.>�vV[[U��Ou)'�ss �$������iNՄ�{$l� ��z8)6GQE�E�?�%�a����R���nuJ��vEE����	�Z�XH�1R(�i�}�V2���S�#�Lm�<"�[������[�LG��-��w���J�Şn~�"
m~p��@7P���Lyx��7�D��w���RA6?�Y�N�PBI=p7Aci����`ߤ\��(x|�=5�r��Q^�&y��l����6yu]-a��:j� 3��,�שƸL伳�kRE�&��&�	p3Gݞ�9Ii��(2R��W-*���J�8��ݩK�D|	꒶F�^�쾉�Z�75MԺ�G\̺��.u�%���:�����.ú�P��r�Ÿ\����AK]��m@=��4$	�~w�5�{�MA��ܳ�?�>%`P��dA�A�\�*@�=���TVqh��#��%54pV�3Nטy���y8妲|��ʨ!t��S�F*V)�j�\JYkGů *P�]yO�m$�L�������XT=s���C���}<����CP���|�� �~r�әn�'����m�ߊߺHCR���Nn���"� �<��C�G�V~�ˑy�Y�n��W�����m��Y��kw5��UX.r�Yt�����	�y�]�+oTs�����z�N7���j&1���S
A����,�N�7��8%_����t�u&/P�Mx@��9^���]Ɖ+�f��E%��Ļ���B!z$�׵\�Y���t�_�{^ y�.ϩ;��#A��}�Y���v��^�=����`�����rN
�2B�����= H۔���d�ʳ:GD�[a�	�.Ҩ��R�;:@p��%7�z��'�Ⱦ�N��oOȬ�s����#@.��A�lr�8X���>�=��7�e��m�b�o	�~��y��Op�����+�V7��řTu-��硈���}��&��ZP��2]������̓������?Œ����B��k�������]�aPe��
VP����!���u��x�Ώ^���g���Zy��~�V	�����6���|tJk�{!�`h�ҝ}Y�}�*y&�DtJ��1�=�>J�Q�ߊ��2_7I�+�)	 ��y\�ۚÃǩ�-G����u����_���y5�5���%)�u���`�qES�K˱:����\�$/T���L�o]�#�S@4;i�k��	�A5���9E�5wM�MԼԈ��(M�~5�%�|����yW�4�N��\ن��Q�~�G̙�5��'Ŷ��S��k��D5Q��Z�i{v���}�A�H9��M�f���m2�\]|��h;61�:�c�`f��T�TPX���kl �q���k-�W�N���4�NA8��MK�`v��J;oҔ����C�3A)��tlJ����J�Z�� ��RLN�G����6�.��SMpt�^5��s�!YXJ>AIh%i��;���n����7�^�����v~O��ʚ< ʳ�H9�g?5��?D���}	�v���~G2��ًx�u��+9�'Z�k��**m[t|R`�1��|�*��n
�|�~*�~��G�gm�U2�b�Eg]�B���1�E�.�S�$O&Bg����@���T�[L�w2SF0�^:$[�&X�&�o49�m|<k��P�9��D�%�	s�)[�TR��� ��v�����A`�K5��k�gE�oK�������;�\L�:�u)Q�te�A0oe�4�泍�wR>7���?7�:l���K�
H�ݴG��{J�\����;܁���J��U�$��z:��G��kR�Wp��UۊK�u6�|dq܉@��L���]<�X����[ց�|��8Ӄ���~
��q��x�[�l�xX�=��.�����A�N8��[��O)��X���UiܑG��=x�|Hn'�"��{ >H�~�H}^*	���4��%���NS����Yp��8?ܢ-��M�fx��4 N '�r#�:U��_:I��'�U*�7��� ��g�� Pe�3Q�,[I�M���-O�����OP��H<�>�-LMʸZ�\�K'��w�OV�9�L�ƉM�PiO�a���k�_��܇[�J��N����G�L]�d�܏��WuqE�x�������L�?]�i?I+�����9+�ٺ_�& vkNBʔ!e���:-0!�J�0ƻ%I!~���}�?��,�S��Y�5��r�w�g�'��9�$��5����)�O�Ϭ���c��g;$2g�?Ǒ�	'F�n�L��5%����abX kj'�Q%Y ��V�3�i{�˿����F�+� ��(�IY��k�ʹq��5,����\���Bw��S��Tt��H��e٥����IP@Y�l�ݲM�D�]Ș�)B�qD���&A��<�3v��czPb,�q��r!��gP0m�%��å��܆�T@J溮�� Q������4`�vw�    c#��iS/T>���c��x��	��-7��ċ�6G�붪���h`fY�5���Ѳ"7��tPRh6$�|�f���_"�R�]9�<�B��s��q�[ݡV�z^C��a�j9���޹^�)df�ټ�?�O��ۙ������WI<���%���J�o��W�ݒ%��z�nT����8�>���ir�װ����:���TR��VwD �VF>ٻ4mcS�gU@,�x�s�&�)&Ǉ��X �t_��=�r��^,s�|��wrQ�W�O��A�&r�T����{�=y/����E�C�u�B*V2s�B�r`$�=u��v�Q^P��>w׏`f>���c�i�op�ϻ�G�Kv��mĥ͋�|Q�nv�����q=tMqVп�����̩�?��TN��y�2�uN\@�)uR=����ƴ��5!w�H�즇����d�6��rF� 'V����o�~5�A�Kc�t<�����n��ᔏ�!��I@S~�j�:e�Z�6҆�p�����R�斌��Vue׫�O�;�S�HXh3�}�j�7m��1�� �֌�Tl�U� *e��<��Pr���JT3{�f Zķg���/qM9n<V5��}��c��{Z���ߖ����U��i�nZi���1��]-�����~J�;�2���J�-z&�ɹ{���?�͕:�����`K�ΫVW�� /h&gGu�g��������4�;l�g�5���=]F�'$�!�>�
��f��:�:�{��gv*���R���p�N ��^�Өyn:R�f�+Ǒx����c��C&P�g�]����f�ǥ���%֓�%���&�I/G�b'��G��H9]RY�����#���,��|� ��q{w��˙��d#�P	N����l`��N�j�NI�n:�W�Cŕ���r%"��\O�n>�{�nO�=y�q�l� �7=?4M�\��VTw_.�Z��v�T�}s�2B����2�6.����a�搚���������E��s6��H�;�����h2Zx�Z����Ŀ����K��Q4�ߛ�#�ZQZ4�R
�_"K�4WKe�͊��É�ײ�i�Q����4� Jw��>�g�>%��(2��C���ьLq��W:�=��m�q��/�M��n_o�5�N�~Υ
�p�is�d�v-7��V�?PR���uSǅ� <-n JN����,^����d%��#��v��r"=��və�a�o��k$�����ڎ\f9B�����M�^�5�Q��.�̧z96���n�9��&d®A����[vi�1:c��+*�0zԭ7k��Uf�Ϟ �9�Z���9c�qj���8$����G�`b��#u46��7@�J�*88�ҷA�x���1>D�A�s�!6���N����&:�aV٢�r�����2�|�0��v���( 7߷�c����ե؀o��y|�2\l{�DXЪMQ������dS];>��ˤT�T�6��[��\�Z20�v�g#�L@�է����8�x�d��U�\~j#^��u��A��/hr�%�f�B.�fJ�O����y��f�A��k���������c��"�ӟt8|�*�5��xs���O9����X[�jƧo�.���9�G��Xg��z�����I�i�"������V0�P+g�/E��rM5lA�7�#s�f�*I8�?��r{~�!�[l~�����-s_�ï�"6�SL��l��=�\nm��/�}=1�%����]�9�����9V�)zve�o7s�I����x�-xNh���ό�߮]	��qݑ�I�磡�tse��m`h��Ѩ����.<�Cd�u��k�4�9���/4A�EK�3=e���W"�ki�52����
,!#�xPQ��K�-�,�Ul����۸���u~ID'炶)����cr��%v͍OC�ͭ��k㗥�I�tY"�y%.�s�9�Ԟ��ߍ��!�]��qe ���(]6�?uo<�����MY���2Í�D�㘀�Թ�Ȇ�O�C3�{��{m6\���J�iYD�*�☖+�SY���(�c��.���N�q���*�8"����u�G����u E/Y Y>k:Su�U�Gl�d7H�����Y���i�X86{�m�q�]Ͽm�q��+r5?(���B9d��d`U1���A��r!�]�I!T]j���ԡqf/�lV��!�#/��M��TB�IH��E�wm�(^��TR�7��6= �8BKQ�N�8�����n �N��icQ][|��[?���xq���$W��p�ġ��R����rQL]��d� w�ܻ�ܣ_tdr�/�&��C	��$�_���O��ī^=�M���%1Y�R�Q��T��z��Uk!P>k�b�Au���o��=�P��`�����%�_"$�H� �����ϗ���1]KpI������|ʀ��s�I2��ߢ�P�L驼5�
�Et���4����ͣ��Z���	��8�ǘ@��߽�EO\r�ʺ�DG�/G�h���b��SJCK/j��d!L+����~r}jP(8�7��ЀkQJ-�^eU˸��;3S��h��Ϧ��]��?���K���4�v����� �Z;���m�SqE(���ԉ�z4m)����C�㲹^R�ȉm��V%�)~�{U���[7@���2g�;��>�Mw�.�-8N�7�q(�D��-uI25T��$;1���`g���V=�"�B�U�����qfMڠz%�.W�*������K�1�����^o�|�Ź]�sj3�P����ig:� �9�(�բD�] ��+�Q|��d��������预&ST�!�)`��B��2%@K����+�PS�N���U�c� ���|�SS�\D�=�l�8�U������*��Z��序�:w��Xb�R6ÝPb��`�K@�M�����g�s�"�.�Ͽ����OYv��ڇ[]�[O�rѺ��½50p�9L��C]�?`�����m��K�/ˇ+�(��?ZU�1����l�ȣ��M�iK�S��4��ܢ�GV�9��gս��`�|���4�9Iq�:�m݂�3�%QΝ��*ө�A��QHRi��1�C��-�<�QWx�l�L���)x��|�CbJ�)�Q�Rt��
Lw�BX������/2��d�|�V����!�f�0��k7*�]=�mk�ܧ�ѐWU���D�}�{�l1j���r�Φ(�[�N�m�_��MX�?�/ݏΎ.b�|��rf�W *�=�p$ۼ��"�f'Ai/:��q*��t-/�a�Z�s#��\�3?�Z�o��ɚ��ĝ9U���_~�����$�.�2���jg�vl�8Ċ���N�c�PS+�Mq�(���s�x^��O�&ˑc������yb3Z�{UKW��ma��S)�;o���@�����T���f.�>���ҵ�����Ӓ�y��e�+�	���%Vj@{���tc��R����O��7�^X��}{x��,gc�,���pz����$�`�_/I{� !�]S|����tEװ[�]ʵ�^Psz�ť�ԛ֓i��qx�-d���)�Сޙd��6Ē��Ϸt�C���3g�˱�$��+�=��z�Ҩ���ݑ7N�9����f�>�5� �4���U5�\9�3|��8��&q��U߈��r�G�W^2�]��@�d5�����h��&�;.�� �^�RR�x8��i��� ����p��d����������ӊ�귡��{�>gGwˬ����N�ڀ���+v]��?�w������۾��߀���~:!$�V+�Ŷ��Ɩ���x���N�-K2�ll'2nI9� &��Ie�k�,T���~��)ul�ei�\jՇ�[4�[mW���fBN��E���'��Cݳ�������Y��;�P�K���"��Ulf;[���}01�m�?O�-�5���;f<�v���(U*�H���bv=ts���%�J�3���y�ʾ���NEo&Ws��/ulO�S�j5��p!�`������q�$�-	j�Ƈ���IW�ӛ��[��$~J��^�.'a70�P1���M��Tw��(����U�nбm��V0�x,���    p)�W��㧶�K>��ڀv�� ��d�/�t�Ý��r���IED�� P�&��*�Nc�������#6 W $u��HQQ�_G5QNGěz/D��v]
��JoB���Vz^:Eⵟ��iI�!iF
X��sP����n*1����y	�ʸ�;XKY����=׿.���D��o�������(��z�+���ή�4^��Ѐ�PC��C{Lԧ�?�e��,�Ms�U�ۄ�6�4��V2�<�D~��3���-�ѹ|�SU���?8��s�����,7l�C�r���Ph�C�T��d��sq��S%9�D�O�R����~(��!��Qz��}Z��P,6���*{W�{Fb�W^>*�;%�x��\>�U��c8��,p��Š.�G��T�S��XZ{R��V���_Ε�� ��u}��y������Zޞ�:"��'^��J��cۢыS�lq�GU�P��@���Lz�JN.O[��Yth'�tzpn��1+U[$��]�*7��^�}%�u���O׋ۑ46���7������7]�����I[���9x���C"�7Wm�Pű	D�f����<1�"�?��:��8\��G��[��}W"�148�}=�� �#d��>7|�⾵��G�U>�ў�݃G��7e�V�]��x���������+�$�'*�5T�,��'�3���|�xG yA_�MM{� �c�ꪴ�����32Q���v����I��X��9G���yJ|Qh)���_���QLRK�I���0,`�<�C5��᪭x��x�>��蛉�4��c��swQ��{��'�#����/�_���'@a���!u�F�Z�q�4ٚ@�=K���(�j��ϡL�:��]�H�C���샋îw���m�Kzv�����F��z���L���c�2�8;��Y�u��O�k�7���ޑw˹�;}!���s�v޶�8���1M����M^k��u��:~���]q���SdRC��������.5`�O�H���v�c���Z�ML�0�{�1L���J���x���'6։����hD��cg�S�0���J��4��'��?��P�����G��w7I9	Ӱ�[�>����U01��:\��SI���aR#��%wQՃ�����{{�IM�N�������HwX�Z���[!�<q���fJ��Mn�y.�y�;���ڥ��L^Nϳ�����S\9���)�%���-;/z��ø��OuX�J�~Jj�6~o����J�.����!��k�ue���K��=��B��V��
?����r����)� ��UUS>�QŅd��������y��$�~I�;a��wL�N�Mw@�+S�q�m�5�L�N%�vK	+�jHN���;{8����O+⼯���١n-��~!V�����9�������r_�R[)Y��M�}��tT��`:h�4L���͓��^�ؗ1(v�]�[���$���y�	88v�x7��HZ��v����F����	Y?�^�k\l/�
�#i�k <Z��Pʿ��J��Q�N^j����b>>gѴ�A�N1�u����ޘPr�Wph]H|�����48��C_���r�xLѧv��p����1�#H�d�S6Nd����~���E�<'p�|�ʬ0�9�/�7[l+�lbWے:49͗����.?���eu����u"��k���l�u@P��%[㉦P/�C��\?�	Idlg9dC(9E�n�6u9�����j0M7�� � !\N�d��j�J�-���[���+�� ����A�>b4H�}�&��1A	c��T���s;!��.�:0p�xW�p��P~�v��]�O�-���R�(���Z(��f�q����꣕w%�jN����>��-�{&���Tz�t-�.-������#)��gV:�J^�CS�p3t�
�L1DmB�2v��M-wP���]��I�������>�_*+�n�[��z�>!(�N�Z5*s�)V}G��ho*r�eP2`4����znr�H�Or�ٝmw��^\yL��x3h$�!�I
�����S���=�
p��)�4��n$�:`Z*�H!�{���ɕ�wn�pѡ�����U*8_^l��p����ߜ��DZ�M�y
���p;�S�\��Ɍ�^�-=6�~`"��G=�&3fݳ��Ku˷z6�r��>ى�V�r�MN�S����.]�(*lS&�T��:T�SsD��J;�wk����?�D�M9!����.���R�ǥa<_�BuW��`��7���Z���3q��MU�-���ꖻ�N8�Ȣ��]��"f�G�����P��)~%]�W���3�[,D�!~�?\/�UD�_��vջNe(INw8�X��o�=7Q�?�w��H+���cB��sz|Z��u�v��wk��Y�P�����~�P]j<l��҆�4�o8*n�.��`�s�C��犁�\k5�{s�A�m��׈y�&O�}۷��I%l ,Q�O¢.z^�Z�n�Ȟp"�N�$��4%�̳�VKl��j�͡aH��`d7���Zm.�:�x�K
�����{���T�j�残;�r�WK�8q25��꧄�<�]�jE9�Mi�z�~�"���SZ�������Y��9��Mh�~��@��L���c�0u)���/5�sd!���
Lۉ� A�!�h�)l�5�E�Q* #@��(̩UåE�"_�d%y���X=j�������?T�ǥ�t[=qZȼ�V:��ٟ"���J�>B�\�ӷ��N
��́�|� � LT���~���S�W��}K1p��G��'���6?�U��G��{�BY�B�Ԥ���!�c�L��S�9��H���;<�_<�X~�z��m2��ď���5Y�|jyp�gPLO��mP^忷b�Ӟp�S��'���\��O��� o���}��Su���6���c~4�����J.j���lAWi���(�"E��W��]͏��w� xH��g+_�KP��c�L�Pdc�w�W4��2<.�R����R�� 
� ���X���?s�:�1�W���YT� ��5+�����j����n)�4ߺ�c�ז�Y�R3�Z�x�Ij���>�{�k��cѽ��q6x��q��-,& (Җ���z筱�w:����(��V���v�	|���v�P�����e���g�'üӑ4JE
)��K�5H��s����6���S��M���q=��P��E�ا�;���uW" ������#��0s���ps��y�+��
I�SL����4�?C'=xp��m���Ğ/�?���(݃oC�k��}���ѳ+�(6�;wM�n_�pD\�Cu��_˯���������Z��b�ۨ���%�$�`n�Mt�~X��K��Dg�����S@Km+F�d.���-t"��n)����{Q+�Hu�fs�7c�x����<(�i񚚆��k�D��8��x9}>��$��p#����9�>ҸfDA"���>� ;��s�I�Qu��|��G�#?|��Iu��,Vh*���~{ٛ������ݗB'�Gdn��QG���n�����P �+������D%� H!ָ�[�m��$�"뉧j�T�nR��4�S~JR��Hez-�G�JN�F	 ��9���!�V����#*o	��}z�7��.`��M�+5B׼ �m�G:�K��K�;���rJ����r*�2>*e�����\�K�Œ�tm�J����mp�`��}�_�BNe�ecT���"^B�.�"�V��\���x\ P�c�Ռ�]�g:�^�&�e<����I��8�u��6�_\�3�=�����-��MGi����6��}S��;
��S��(q|�r�᧥��>�����k)�f�xE��ZV*�AJv�2���ޝ2�p��6��@=����O!�/�QA��W�#�a�����N�稵�R����B��W��w��7)��)��!���G���y��x9�S#yDuۂ�x�$".�H�m�W��}��<�i_������Xd걲�姪    ��	�0w��"r�f�Z�RRԯ�W]D|wM{�����B<�Ch*V�`̋r:H�~�M*?Q���w�ذ�����^+cޚc��>d
=p	Gn��mB C���HvT'mu*W�ܣ�?��;�Tm�ʹΜ����Өn/5�t_!��=I�����đ��&���n�ln��_*�'upH7���wע]��
����e�R?�5���6�7����{��8�s-��B��t�[Z���_��a�v� c�}�/�m�_T}��޸5� ٩��N��P���$�a�m%�{����[we��X����=B��9�+TBKO�6�}o���n}o R9���Z�$��~	Jk���?��I��p�������J��#����"�S������vP�O- &?�e��?,���l�<�����+�e�h�FP���3���Ô���~T��P���K`<C�5��g.� I.o!����u�H�ԺbF�m�Sl�'OD��[1�P�'�I "me��U=�Ǚ2{����f�z@�@�6�m���Y������&��8�ѯm�:�q'�O%���F�4�M=s)�Z��p��v��s}S#�^Sog�L���z6I��Ƹ�I骚��A�Ĭ[�e��z��eNj�H�b���Ȇ��K'�"9��)���BOWe��4W������da\]u����-���*����C�)�(�P��)��!�9�r�ݧ�"W��pQ�k:Oj��14@ (�X*��Uc�Y���s�R�����Z��UY�g�i�ϖ��y|����á��}g��?�F��E���	Z�C��=!1��	�$�|\�����F\��"܉���
��`��pT�bV�ڥw�	(�|>-�����=�/j���t3�f��F>s�TrH��ރ����6��i�t�h�|+��]�G�ȳ�G��TJ-�*a�'�\���h�x�j�]�-�$�
��c^�r�H&n�;H�@�|5F)t��/%�Ⱦ򰳪�KIǩ'9�j�:ߥ�N�V������P��0K���d�'�'�������%-ŭ�Ӊx����n{���+Tx���MiM��|��%guV���~�T�eSPi���,JYͦ�I�Ym[|X�8H(�~c��  U�ؠR�*�F�t^��Ef-$�Db㴓�R�H�ɷ!+�k�ܹ�F׹]����ZxԐ;;�$t�Aӟ�gY��x�9�L�i>�l__�{|c�໋r�J'��P�O���|�4+z�V���q��$��t�X9�?3��:��w�Lt_�u�h��֭�[ Q��x8�"��v���Y*�NY��Q�k�?��g�h��ݣ�t�o?N�ɣ!�yQR v�P���U��j^m�c������;�,�|A��r�vt>~��U��	$2$�on���n��(���bhw���4g����{t(I��J���zHHݫ�۞̬���Q&��:�����#�(����~�Z�S:?�W�0rUO��z�w,����nH��KUU& � �(�(���'��]9��tD�ޥ*��B���TgԬ�QE���Z�)�����0"To�-ն;.P]��VT��M2����P��|[�ݞ�oF
&ޔ;LU铩CT#3�	ܶ9B��0"q�CNf���_�?(\�T�a��J�~K6�����^2��U|}�y%T�1�s @\���$?��Nq+��sm3y�-̯8�Y�2hI�o���������"F̷�t�Q��J�5,��̢�0n��o�.�&9'+�&}nN3@������m�ɸhE=pM���̊�;l�
�Y@r�+`D�M�9��$�B�tr����t���gjا�h�r��H,�W�l�s8��Q��l�O���	��dF{�S���:�����RW�q����䣄�06d��(i��Įy��fEV���!kbk�əv�ה8��m�9,����V��d����?��!|����2i�V{�[��Gq�K����z����ΩIc�S;VS�?�沱�����k�i��/�X};b ����o�5V��,��C��������w�_2�Q�g����[��7i�`��Z&eE�-�r��WsL�*=tWN�����j�UyD
�3u��!�4*y0q���r�w�Z͎	���7�w��D���-�+������@�ߠ���g���u�����Z@4�f�};I�Ym�n�I��,�R[+��0�W��]�ت�z�E��$F�c�ߑ��G+�:��wG9��L ����ȩ���ܻۜ���M^KUjK ܛ��v�Kj�Rx�h�Bͬ(}T&��q��)EӅ��RRpsU�CŤi�_ ��M�Po���x�p�A=#3*!w�64w3����vjgM�|�-LЉCigA���6ҞO�Mt���No"Q�j�p�/�h3���y�z���əwS������A���C����4��N�vH���݇��V�.��0�c) �J�6o���ͱ��0���'~5�%�T�)4�g�%�{�ʨx�y�"��FWWM_Iqv�Ҫ/lqy���'r����d�����9�wXF�n�,'K�͏&�Y���� ��R�&��ô������f)RF��.������gq�v����*O'�z2<FZ��>U��T�-��o,�^���"hjI>�N���]>�Ωs�@uh~�&Ѯi�f*�g�9;��c�|^G�=��V�w�j�P$wC�l� ��@S�g+�r�Kw����Y�wxr�wWa ='�z�Z��\�ۜ�q(|���m�|�|M��>4-��Y�i�"���ܮ1�U�M�e@
�3������S�����sS^j�U��3f5SދS>����r��^kj�o{��c��U �F#=L.�%E�z'N���S��89s�e�T��֭o�.��E�~���ސ�?I�.���8U��ⲅt\ ����]3Q��`-�.�����J%x�.A����	�Ϻ���9��,����vP���*Q��m��Oi4E$:_��"��lA�Wh^���oݢGQ`��Գ�S�98���ڦ�o�.��;�?F8!~ia:����ǣ+Y�rT�x�)�Y�p�8R�H�O@@�y���4t�Tw)��E�ͫGAp�$'�Ը8�DSu��Uz���\�П7��W7�R�ݧ�)�~|M7M
�*M��J=_�7֩�SQ�i�;��#�n������#�|�R�j(��E��p��3*�FNW���S��ߡ�����yڸqw�ʗkS�pR�_ڱ:��d�>�\)o��&�S�葫��@���B��>�;�_\]7�ǲ^k����&��`�8�m�'�\
؍�������r�ʱ��c�qU�D%�8��颮�	/�%G��{��
9F>-�4F
~���H�%��I���֮��cmɦ�!�D�B�ke�R@����2��o���d�ˋP�q���M)�����%�m�ʰ1�� �t�S�v'_��6�6v����F�2�(<���$�:=�V�A�PГ����.הNE�I��k��$Y7Vg��p�W���y;���_c����_�H�*��:y
=�VE��b��� Lq�l�֬��#�'�^�~$0���C�{�<�����U�.���ھK�m�;۶����ߢk��B*:/�	0�W<s\������7x�2Q�h~�B	�B&��;�C��d��?�K���j����Ei=~HO�`����~�
�'�#�9چ���͗�����zp��D��9���!�����꘿*%�{��@��n�P��0|j���K:�}�U���+Z��@l����ޥ<b~}O^�b�M��xkT���Ly�g�-�xT�uZ
y5M�뺧�m����]��4f�G���ay�x}5����]]���A:�Sc>7�]�5'W![X�'�M�)
5>��?f#�d�|�g؏��^�C��c�~g��Y(���*�V}���Z�.iE�u��6U��)(�o����{Y�}#����w%^����ضt������g�Q�'��+��:,�X,�&�����q�bn|��_m�˹����u:���t*�B���q�/����>�8&��|��A��e��2�N��GZ'�];�=�箣AX��B�?8<t���ߊ4�?ȿ    ۇ���|Jg�h�-����
\�ᓘ��qQ��`�G��+AX���9�r�)��a�	�ևG멠JM}+R���$�M}���
�7�iaW�}�=�4��;����@b^6��MOu�A>®��
��9 �p���5�t�.��F:�:�I�B�4?�	/�Q�"��q�q?�ő�)�!��:Oݫ��K�~Å'|�����
��~��5��M��;+a�����2�V�i,���� )K'35��BLa�|�|��6+p �dh�ŭ�^��\5��K���C7����핻U#�)�=��v]��X��
���U��撠9�*��C1��9C;ōY鬱Q$!s��L�Zg,�-����� {X�oE�h�vdЅ����(�o�	w�Z�T��t5�9��
��§���P7�����ˑ�F�% ����~O��5�Ȇ�I�pwur�;y�8b�%�<C�@�����~;����1�Vş8Cjr�і���j�Y�$ޕ0i��nv���YڇN�ɶ+�{�7> @K�"<Wv�,�n���JV؜�e���[���)��ӌ�T����߇�>Sw�0�w߁I l��}���=��vB���7����T\�`8��V�
�-f4�m$���piUw�_�P����?ŷR�χ�ѫ�%��+���T�����:����߽b��r�I���d��9i��
�*E�2���s���W��(�y<@i�X���#�@�PG��lz�3Zє��:�~"
��Ǳ5�MϚK߫"/�H���
�σ�Q��h~!�&����[�f0��sUN������l�+"Z�1:�TΉ�p�
}��Q8�w�F�pc�Hn<;0�Z���+wBye%����dS�^P���<R����� U� ]��(�4s��i�e6N.������'YEC���Q(�����<����5xB�<����zL�֡��-���sT=5h'��1fOM�" ^t�%U9?H���٭[�P�!�q��������<"+p'�t�(�5wW~;� a�����y�r�lԵe��$h!�b�p����k���la�ۢ��4?0ۻa��I+�a�EuY�J]<5��q{,��9��ۇ�3M��
'^!)ͥK�
����8H�Խ���7�K0 ��w8�ذ�p���s�5�X��?���U��n,�u&/�/��yZ�d�ӭ�zv�?�SUNw��R��?�^"U},��e��r����m���-5�����!v��W�j���iDs���������Vd\?�tc���X.�%_������)Ψ1���:��"h��n��|O'�)�{����6���}�l��O^�o���%+��MV�7�A������S�@�Y�1m�R���3�u�!_�*<�p(���$��T���ǽH+T�psx�����+?��N筒�m�n�YeVb�k���گC^&h�X�E
��L�+vV�bB�l׺�?��n�� .*S�J��vz�����)WxsWJ�"L
����{A�ݴ1�V�Nv]Z�ݲTܲ��5�H�1�vI0�UY��Z#�P�P��%f]uQ��.�Ų��S�{��Rr;vԧ�u  �l
���D��	Hn�]�bP�"u��:4�.�äE���-���i�&��>Ȗ!�8,�����-�w��@IB��������$�s�J���#���?L��h��-o�Q�ش���)�f��[�Y�kv�j�F�OX��U�돒��}�J�Q��
\��]���V��/]4�8+r�W�����.�d"�(0�3v�tB�����n)�~��՗��^k7�*�l��/�K1��t� f�m���x�{J�ȦYw�L��5 �s[݁�#b2��-w�����ܭ�'D@񛇔���j�>����r+�3��2�>Ǡ9���oN�5��H�<5sϮސDJ��Y�L'���'�u�tتK�r�~gi{������v3�+�"��@�!��v�������;��{�c����ꥫ��ey�a[�\L�G=X/�'���\KJ@i�٪j���^橄7��]B{qw�6-�ta��b�P���m�}�4 8���CZ�i�һ�G�I�^�|�]�9���HQ�QzU�@"E:V����(xt�����)�Z�q�>3g��M��Y�)B?S���'`����(?Wl 5g���! P~��{���5�E����c���X�7���f)PO�[�l�Q���ߪA���gF$�� �`*{�Ƶ�C3��lR�?��س��V�
&��yE/D8��������澙C��p�4��~Im|������NWQOl~?�]IR{r�*��ő*��ۓ�,$St��;�A�WB�����L �ҏA��%�~�������\GԣI�U���1��Žb�-nj�Y�{�E��X�N�i:���U�IYH�H�<���y����H�F��'h�ۤ���&a(H�
��`P�P��	�;��b��X�wR��pvYׂ�e-�k�\�[�(�O�`��l��փ]��˿���j��#�O���:�b�B U��OkK��=�ՔrYG�W�DH���[��c�=i������G�8��#\0�20��K*�D�4]'�vB��]�4�G�
0��h��UڬGt�/�B|JW�jE���K�c3��Of�;A���j[�O߬\�ه̼Kck?I�'�+`0q�TB�P���~I|�p����֊�`*��u<!�e���,�֓��e�v"���}��-"/�%Н�փcG*��(9�_%���������'P������]d�{(MV�8���ƛ�����L��������g���7y���1|�ͭR��_�����}���?��ou��-b���|:�{��������%���gh���?��%�4�N�RʯI��Z����`��GϮ<���`qH�n9.*r\�V�S�,�]!��i�&礐�[�U�G�nϳ+>�'	ҏK�|h�S����X�g��VZ��4//�;lY${P �H>n��L�/��}�]��8�'���2''ؤ/I�#�k�h$��0����G��.B��/p������������C�G�T��}��[��jo�H�������I7Q~��iǌLr\���ح�K�M����t˒�"��}�J�5�8�4O��h�������o����-<�n�9��-�RLW�*]���k�~o�>���}��~�BJ��(�|�4�ԕ����B�$�&^���W��C����t�z��+N@���n��.�.?�ĩ'��g� L��R5����z%����lVko�_+�6�oEs�S��=�ޖ���cvQ�`i���2��3Y7�g��{�-�b�&yPݗ�iL�"�fY�	�Uv07��n!T�����K��C�>�������)��K�;��|��-��~?CA��C�j�"��2���nqd5}'i�cl�E�^D�~�M�b��H%6A>�\E�%B�vH�V&lͮ��]UtQ��ʘP1��y���,����n�E�qRe�i�M�&�]=R��,�SƠB��+5�X�ȥ�
?_���J�J��'	��v>��	 �V��]
ok�d�s;#XJ �鿖��Z��N���<�M���T���Ucf��Dm���a��B�b<wBa����ߩ�K�ዴ�w����*��dvo^f�Ǉ[���=n������T�'p�,�=sXH���hS'v�a�L���c�˓w{uH���9�=���M�G�	����	`j���ֺZ8re4WW�\�<�KD�d�}S��o`N_�[ٖ|�/��y���ǰԁ"�+Q��u��_�te�NS>#n��w����RO��F�>���T��{F4z-o��i�4`"+iA��^����{�ڼ�?�Qo]p�:��� �t�ϥ8�ipz3�*�Q��A�׿i���V�.�J=V��-��$2���4C�?�ӝ
��AhR~����%j�C��T��k�%?�۾E3�pm��]��&T���52A�j�Ј6;��H��,o%V�=s��A�^\�|��>o��C��J*^���϶$����|^t+�?�X���+���1���uOpwح����=�[6�2�.�qw)\vb9�g����:    �wA���s��D�\=Tk�u_շsIts��Ug7����%�;4�+�t�z	8�[�7@����o��^"��^�� �}���N����z���]����T?�=� �m)JM�Iע:�Ah������HN��
�j�r�"������G�~iN�ܱo�k����A���ѭI ��.��q�Һ������}�CJ}���<?����-������� t��$��f�T/���w��Oeͷ	E���W� �F�r0W
��B���G�{��Hx�I�Q���'�Q����u<k'g�$�����t��w(b�]�HV�hj�/�M�����	�w�t4��[��`J"��VS8�Z�ဈJ�M
}�#C�N0���.��H� ��V����3�k�뼕������:���\F��8��%ᕂr^���&�;�4����/���D�9�Jh :��g��L6�V��1`_�ky�Yt���p��wQ7�?%x]�4�K;�z�y��\U'(���gx���ʝ�R
p��^��R�]/�Q#�up�W^ytB�T�m�"��m���%8��ՋC�prm�_��zwQ��������ɽ���� y �?���.ܪ��&��������쟎+���f�������z����i��>�ٙX�g�J$��4^��	bD��m~<
}���"vP	��mq�N��q�"8�ޞZ���5���^������P�rɣ6���#�� ��r�]�8ѯ�I.Q�J�o/�j��q�:�3�~3k;w��x2����̔��<���k�����OR��i�W�)4[���V�"LPM�-8��I	�|��Bp�UY�t��v��Hs鱫j��"�:��=�ÔD$�88n��Z���Cp��bs�Q����^��'��[�EN���Յ���*�M��3%0Qzun��j����[5$��S-��yH �����I��8	&�ݡ�t���ɨ*Eu�<f�R�)U��z\���M���+��[L{W���o܅%��� �A��ȨC��y�lӆ[�u��]��-�$5��������Z�����#��-��������n���kZ�K�\qU�Gx|��J!��p@�Zp�z7��:���?h�oK_�&���R=�J�PF�*��j������?���ܜ?Q��
�NI]�!<&���*��]�d����ڹGK�"(���"�և��
�`jM�������^ :ew�*�r�K�>�%��uy"A:����.Dj6v��_�����^�^a�'��z������1��L�lC�'[�=@��u(��Y���|@nީ��)��*Zh�eYƧ�����$�#LU���r�<윑BxL{ٝ$}̦�o���`��5�u#��ٓ2�������!�%����.�7���9z,�k�b��( PٕO�P���
�0=�R���#������I�ќ@8��ϳK�m�k�2}��lW�J<cS`Zh��X=S=��K�֘z�j2���m�f7�x��<��� ���H���N�l�_W�.�ug�w`��`���_��	ڲ����!! ��#���j��?�\>�Ч<.���߂/�$/�eҜ��=!������C�~������K8�lT-��h��1�_�}ާ��S�Gް^�.1P������[$�ew���+�q�$y0���8��UH�'��2�pD���q�	����ؿ*���!�y�U���ʆ<Q�r��x�]�a��@d�k�CS�p��ў�F"��fy����^�k��V�-U*�S93�׫�3��>}�D���-ǁWPc�2�hj}ĀjWntk�K��ܨ8��js��W�C�� ��`Ɋ/{�ס����q�M`����E�* ���;A�[��]S��4汻�/���e�0��|�&B���ܶTG]q�BGu6�-�Bަ��!�\O_g�A�����o��ڷ+A�~8H������VP�K�����V����� Ѣ�6Rzи���l���fI��¨`�)/;�E��쉛��I�J��)[8������CzHK�3<��D`�B�M��n=��&��3�Л#'��G䬟j�%���U>�-���k����"��� =<J�p�sz PY}�aנ�|����𣻑Įx-֨1A�=���a��-���H6�ܕ�øƿ<tn9�A�	��gIV0.bh���^�F-0zy�a�?��:�1����iq�FH�8M[���U��d%�p�k�iL�TB\�oB!�S��2T�����/q]t���2Aa��g�:,%�\Q������B��>�g*��گ�F�Bٰ烸s�.��E�M��8�ľ�VY+�V�k��S�b(I�<u��>1|Or:�XnK��<�B�`a�Xٍnݚo�H4�O�a��tq�4w�|�SM�s��bG't{�����)�n�נ|�٦	�f�e��(�F����a����%��Eo?�v��l��ǒ��v�o���\rwy��#��������{[bdG��d��$)r�J`�
��A@��5����~eQ��K���#0��+�3��2�"�j�Ka�6�o���C��@�	,^4qq��Z��?G�����]$�l� ����ֹe��c��?����Q�ְ^]��y.�jX~���;���_�!7["�'.塃Ѐ��Wv'��6�;7��5��+�s�
�
 �K!N�?���qZ�I90/]�����$�l�qu��@B��:����Mx�yY�'͕usS��FW�rs��?�7�I}'~�Ϳ��!r��(���K��.�t˶C˴�t�ٓ����(�ެ�<�E}Em�WX�{�twG�*@���s��Z)&����B-[cbR�VP�ɖ���<T5v?�M�)ʣ`��n{��~������9�P���A�ݑs�1|��ß��C&hj�д;ܤ}�v���G�Ψ^1�[��Ns�-�Ì�����ゔ�yQ��P�q�tPxl6:\���P W&��v� b��O����d�������5�q��T���D�8ta��TP(9d��m��gǩ�XnwR�e{�.c͛���+��9QZ	�����4̓Դ\��j������qgjˉD�?�J�R1␜;?٧:�?
�u��c/&��ܠ+; ���_P���֫D=�[ڕ�ֽ�I�EU��.��u��u>IG�򄱹�iY��T+4�=$�у�z��ʇ�iw[��qJ�Y�@�9d-�ޅQr���jjQ�M�_�q+ASC]ҤZ"�S9 ��*f�m�E���fv�?`2�T���wAχ;P������u�溵ٹ �/O��Mt���8��围ӦQ��y[�Vf����҃5��N(Y�1L3.�����
;�����B��1#��t٬����_4G+E'�i�K�a|���KT'O���W�˸�p� *�Ȉ�I�y}O-�n��O���+�ʻ;��v��<�,�7e�:����n_�b��bN�k��nh�����X��&������W�rPV�C��F���}����r��>�'�Շ�� cg�z$G'E��y����7�8�¡�utn�c������s�?_� l2��]�*qV�]L�k�3Roe/�î�ĥ�]̢f�c<J	�b�'{�YW;2�^�����c|�K�ՔF1��c6w�hM�>I��@9g?�'�0O(B>�����Z��)�$�A��E�jD�\��c�~I���U�boT�Cn���zƝ�Qऻ�YF
�,���]�B��)J�۵�33K2o�]�6+N�y�`�_��Q�ڵ9h.��\�X�:N�I�na+ܰI45\D����d��=��v��Pk�HR�J÷�x�5mP>jLQ�m*�E��/j�
&�.�t�sw,ĵ����-{�ˏN��P��:H_b��Z.lJ�_T� |���/���r$W�H�^�����!���y�GTwWUF�K��`F��	��y�H��-H��ߊI5��`��h�C�8���ť|IRY�m�&&�l�&���-�^ǤK���<=O&47��!�Z���O�_���ּW�xi&JU�a��
��ک��S    <~:���O��Kl�>It�@MFH����f�����1 �����m�w��a��`Ǧk�ȩ�������s=-�u�a�I�%at�:��3%r��"�w���Dz�:��p�F��!Z)��7߳Dm��H�)�w�XŃ�S��͗����kp��1�p��Kdjr��M��~5p/Cv��>�uMeU�Pב��k7���7�h�X�W=lļk�9Wi�/F��K("�l�;A��U
��ˤ~��v�6H���t�V �{Վ�4�)��ru�0^�Ŧ�<�`!bԕ�����R�ec�S��5'�WrY�N�4u�Vy6���I��ц�~����Ϙ�F��0���� �s���M�� 1!��; ��?~3R��]���H�/��l"�<��Ն���;�,�4�D6���|�"J���H`��6R���լ>M$��I4��b��x�7 =+ �����X�O�������Ԃc�,J�g��\#�n�k�d���\]�y��K���-��j0����Qv3.�*6'�䫽l23�f%��9s�S�.#�x|[`\���J�&����یG|��]�J�>o�ԼKR�kG,��A��-b�s�	ќ�&>�0��3��n��צ.A	K5wBn�6o����"_��)`�Pc�`��[8����7z��FX �����U��
���!Rޖ��0<����e�گǄ1�)��^t���ܴ���7��ms2
c�4�Ց�׸�|��!�v����K������\�r0k���JӁ.����Ϲ�K�w���˚�r��O�G)�d�9 �l����5~&�ܟy��vtf��M$6�e3|RŁI$�mK��K|�Ə_L���@`���]����m�+J3$4B6��t�r�&(��@��^�K=��~�t�.9��إIuӘ�J\
��"2qb�[y)I�)P��#�l�p���>�{�/mƇ��\�+��Zy��5�
��y5��>�O��L%�i�=����F�g�����?K�ƣ�Q�B7H�4%hgL7}��(�4kn�_u�^�u���[��'Ǵ��bS�āa?��(]���]�y��s�u�'P��
\x��F����W�^Sp��(�
���K������b52bOL��Nۓs}q [~CR��sE���
�MyIZa2�����E$�RW��vI�J .M�}J�����W�b��&Ŝ��0[NJ�9�,|(���g�W7�쌘���P ��H[T�։�T����a̕�}O����O�5||Be�1�oٽ>Qk��ǿ�@�x\�Am�j�E����y!���$����䏠�cf��ˉ��~<��\\C�YH_Ĭ?��e��Y��7�`�>��V��\�����i���MTqm#1M.y���;�p��RR�R��:�����(S	R�d!��֧��XD�ZZ*��]\bڬ�>H��Pc�+�,@}.1���ߣ��u��=�t���2�'����c�_�$�_Mt����K�|�f�5Qa��u����][j�5?M�s�� 	���*x�{�y���-��ּ�䋠ۍl��=��-�-"t���l��<!�&ݴ\�|��f5�'���r2M���w^�s`�e���мC����r)g���������E-��_�9"D|��[;�b�J��ʥ��۳
��X��ڗ��ݱ��R���lA ��m:E_A!�/���1�a�߿��H5�5�/F�A-L���N�4ՙI�ݍl��h�ˎ�M��ԉL����<�o� ��kfohŕ����e`��}��cƃ�kx�r��3?�nEe���Eʺ�ΰc�����yYG�a�g�� �?I�5H���Zpy?N,�z��m��}��-K�$!���`49��@��O����	�Z��9H�QbVn2����<�s7fg���=-{*���U�UTI����b	�G�噑yH��ٱ<p&��@���$�U;� �S�
�d��&�<9���4�z~"M�'�9��
�4����2��`C�*�tN=�}$4X�=;[�WM����_���[N�V�.�F	�|~�4F�/eӭ�MV�e�=�
�T'=��h�Z�V�ý�Ӌ�����YJ�/��}6����<s��YW!wg����Y<�)o���}wg���R0ע5��B\S?��;v4���bm��/[ȭ�6^�T�㪮�.�������:5���_t��1��vk{}�r�QC&Ң��J�����[�%_�C �2	�2�WM���v;B��uU����������q!��)�R�b� 
|l��˃��l���kqI��\��t�U"ؒ\ۋfˌ�r9�R��C�)�V�.l�
].�>�5���i,��)kFĆ
B��lGL�80=b���}��&�?&�	��<^�_]�=a��(l�/Q�T�y��R��&�;�6�yNS|u���ca��lM�b]P �.Y>��%Ƿ[Rxj7�=�y��C��I �Ul-Zy�sH�!�/YF�����t�[y_'�uꬢ��l���n��O�鴋���3lLLwl-\WZ?ozԤ�cr�i}E-Ȉ'Y�1$����fIf)C[
øu�Z��	XL�R]#I��o7��6|�_���~��ҹYz�0�e4�R�\6�\sD���V�Ml���6�KS�T�"#�1?8uKI�h�� %�3?o���,�V����;m	�[{�P&*��,�mL�_�d�-�8�]s2�c�/�֗V�Jq |��v�N=�m�������}��4F.��'>�:7�ȥ^�%o�7i��YL�?�?�e\�<�����z-�߼wON��Hͻ���k^H���O��5'���?R�R�`Z��#ãKq���w�����H�{Z�xu0�|�����-��p�*�i��J� �y3���������0H����L(s�,Æ�q���U��9.��IM�B"i��4#�	zMEw��3�̓��A���X��x^�籜�@6��W-�_|��xm�g�MꟖϭ��ڬ�%�\������
�tʓ�g�����d�S	�N��K/) 1�0��-q9�u}����I��D�9��S�ϱjX����v��Kb�97��L��`uh�)��|*�S���IO�V����2�发���p���9W4	����u~�˔�@�b�q����8�)%?�Cj��m�5Ô+AC����J��J$+�.�Cmy��M��I7WtLpNTAi��ޭI��,ΐ�sY���"7&[P����8Q$<�[O�؜a�����W����)�r��w��h�AÛ�Ӛި�4 �j�R�1�5qF�pb��a��%r]�'g��*G��-���Jv>�-��@ײɌ�M��]qmi-�w`X�%��ȩ�KP��fII����QB�,E���@GVJ~���_��M��Cfu#kgKI�%�n�i�n�"�WC�#�9�"PKn>9s���׳��_Iȍ�u	��C�BH��I���e*����5�j%!��lk���5yL�r����=�� Q�ɟ���8-z�c�����Z��}�r}�ӽj�#zJ�|���d�J���Tm�,��0_��IO�N�~ ��}'��NHٶ�y���kZP.bAM�����ɼkg[5��?�D.��Sb<�j';�7�ԡ+����5�aK@�Q�L�nq$*��%!��7��tR}@���m,e����'kמt�Yp:��/g��`Ү����+>ek��������+�����fM>u���?�y>[����#�i8�4nyd��rd#y�u���'
%}����n��JC]������D�����P9�lNE��D�s���f/���Px��oc�q�؆nA��1o,��h+B�}�cuv\��j��b���	*����N61�P���[ˆJ��*�:�`�J��R�%dw�G{b���j�Z�z֫��x���|������$8��F�?�mx��f�V:X��v�UD��K�/?aM<�h����A��������o������˿`����J��,+-�>g��?��uuqs���71�M�ax�A���/?>m��6H����|^���D��\�y��X!��ɶ����q��6w�Tꂷr��"gS�eS�\��(�K'�O:�>��[�W���]a���ɶl���&��]22��    �#;&�|�$i�Gǫz$��?��wC���4\m⣤�W)9��w�oT�ߏ�]�s�,��z�|]G�f ϙhp�^"�<Z�3!,����i�!��=�������=|\ ������]G������_(��\��9��	�������5�?�c2B��K�(d䌿gE.�����\��XMࣤyX��g;�V*KD0,��"e6M���.�2Tӷ$�}���~�������_w�}W��w��=��R�O�;���$�$/��5�?�߷�lbsl��5����S�S:�x����N|S&��|�h#�_�6~��;F�U��s�>W��<�vg)ML�fi0�w���U���D����}��8�� ���֐���h'`-A�|��+P(�SY�Y��'�Ku2��ccw�v�cA"���S������ ��$�~��иn�����O�c2�Q2��z9F:Xo	~�	W3@ۥ���%�5��[�2�YY�"m+?�#XC�6�>������ؑ}h���+��r�Z����sg�ۆǕ3�X�["µM	'���]E���vy�Km3<M�MAI��2�^`cb����b�84�-@� Ŀҵ��>�>Y�7�����+�|1��W��(#������ ��L��MM���s����޷~���`�N[�����{֥/z&a�t��۵�A_�R���㩫����I�Iwh��n9#�)x����|'-�< ޖ �<�ѐJB;���$1�@6�s<�� �8f���[��1<��U��K����9� O��ȟ!�n���g$��N�0��Ok�)\��1�-4ں����g�|��a`Y0m@�G�yAHǭ}��_���$�b��c��������ڧ0ݻ���l�~��-��RG�>͂�O��?��^O�DORt��9�����(:s�?��h4X�c.k?�R8zj?v�(���lIr}�3~�L�	AUm��r]��7�3���ج�Ò�(��n�!�������t�Ҽ[��)�i����
�пZ��Z�`M��|����`]�'�瓭K�7��&=_V�v�hH��4�r`�ts���U�������$��Bɓ 6�����\Ԟ�v� ���E�/Zke�X8�>؛oE��0?y�Ϥ�ve�E��2W�)xs�N*����^��Uk�A)�P��9�3c�$�N�dd8n���s���8�����{�ﹷ-U�9�j�ki���A�q��7}���-EL�ʀ3!<�*0�R��$s���Aε�K�<�ԃ��-dwB́��n�m�z���I�,4��-S;�bAc�<�]��H���E)� f�9e��v=����1����dک\���C������4�r1��Z��5<�o��l� �F"L�<�T��b���,_ ����V��֮�g������������O`Lf�:���8}�U�����i�;6��I��9�����!C��4Ai��~x����23��ء@�HS�ZK�C/�~�ו�����үm�lh�U�c_�ᴦ�O^�8�o� 0R!Y�)	2%)�ϳ�/�,��r�s���G�Tf��C�]m�9�k�i�E��:��g1�X���
�8����v��Z���4D�ɚ��'j�#���Cr�;�s,"�#��h�p���?�<kV�����.g1x�$�Xf-{�5�R�&`���2]�:���@�c�8��:����2�q% ,�I���¨a��@�	I�*���|8?�=օ�ɵıx�'��$�cp���J�!($Ȕ�6X��	����Pn�L�p~�e��QU(�4R33AL�&�d���OY1�V�o9�&9����t&�ե�������y�-C�}�՞��%b�C<vu.R�m�n��*����y��ˊ>�`'h�	�����gk��4�^H���u~ �g�m$���b�f%�
�V�N&�7J���;6�m\�`�N��Oj�!�ټ}����5C�e��@R�P�ě�+���h�A���,SF};M�<Ԁ���1���-v$q��@5�>�c�I��1����?=�?=�_j�y�I	�4�s7�wZ	�%��d�R]�X�W�s���Zj��A���TGɣ�i�;�Y��B�3�E0`��^��A���O���
���X-�6^�c*�-iw'
�h-�[�MC͞j���1���b[�qS��ֳr��6t�=�Ȃ~s򽚩�&�t��Ā�����L�����]��g5�ru�fl}	v�A�9�)�Q7=�k�f�;U�f�c���p%4��7��ECNw���9���bp�YʧTH$�$��K��^F^�ɇK�)�t�W5Bw}�	'C�4���������B� hm.Np��U��IՆ�9q�U����X����=�/{f+�G`p�ߒ��+c&f�4�(�5�]n-� ��ٻGȁK�(r`�t�6���Kll���Ȍ2�Ip��P<%�4̀)_t���v��~��w����[��O%��^ۗ1�f:%�P%�1�j�G�_W&	���ny���`ڒ~�~��t|��`�W�1�h��'�x��3��H�Q��*����Ŵ��6��P��L&R��	��S���ΟN�"��8�+�`Ցº���f�9,bY�E��:%]w8d�]�������B�1j�jͤ��Z}⁣�]�j�δ��,kb��?ɹ`7%�&Pt����}��cދ�7�$_̾�82%'}���ʧ4�R�TM�#w�6` ~�TއD+)�I�Og�kɃ9�%������圖�ٔJ�xN�����
��i&޺z%W�ȥٝE���vJ�u�j;���i�]�2�����(	��%���f�y�TJ���U�����!�0b�J��}���v&�[G��V�S���6�F�S��y�9���ZJN_n�@����p�̗2�+���ގ�ngGms0+�]R�m�4~h�i�p�Y1B�}��9��'>zl�yL+v�͎Z�	�S�֦Z��KL����p<���5���-��Ve)%�\���6�6������&��6^�Ÿ#�Z1�Z���7{i��(c�����F�L��2��u+)q4qG�	V;x�*��[�t�i�̙�1'Z�L�����x�5c��[\,eA�������W2@W�a� ,����ݜ��Ĥ���@��'R�[�^%���L�|(}J��ט� ��y0a!1�E�u�b���W�K�S	y��6;��6%q`ƑX������f,�א���N�����p�o�5��J�u��l�+�<�
��qQ����z�2U�(q�����L�LG�ap�Ǻ��*���g�~�j�<��B�|[����j]��+���#;ڶ�O���� �8�R0���(��3�H��^p[�5}^p�M���v v_Į~H���X���>�<"K��\��*O*'EM:��-�B�}�?���1qP�-��@�܇ڟo��uW��UD~U~2�r?�Q=X�LI1K��<�7��7�|��f2 �$h�{���+S%���h��?O�k'8r�l����/wX�	"�[�:�U/]ͭm�|�z���{�c�>�(N�<kw|�'�"�o<�H�n,{oPx���KI2%5;��6����J`�5�:��lS>y��;�t�f�WI�X>�9��G�l�Ac ���y��V�#�f>�1��@�J��`�ie��D��g�<�ɚ���cN`�s'�xC�䋉���5�2����c��:�<��f�g�Z+��X2�y.gi�����$��7���TO�� �D��bp%���5|��+�/��I �ӧ��=�r�t)�sjf�p5�H��S;p=��&`g�N��5�Z�r�,�ќ��J�b��|[�e2�W��[�Қ�3hk$	�\�ˤly����Ujr3kƌ��_EXJ=G1h�tI�>���h����/f�\��%Vk}�������z��O
�|o�^�E�Sn)I��z[W֛�G�1_��w�� �����L l�	��d��#��Ng��`
xN(JJ�Rx�$��Z�G%��d_s��c<Q� �|���= ņ���8l���T"�u ;��d�;5h
�i
���/5T;��2+i�g{ʰ-��s%�,��D��0�L��~N=��k���»rH    �9����1��Z��-�N=զ�ܺ9����OJtEnC5@�JI}���k�f]h#u^z;�NML�eZ�� C�e�N��W6e�A�c06�޵j5���JITV/ۢ/E���}��ߎ�H�z��;tya�5ڻ=k@��9�zE�۷�t
^�g�\�y(ڞ����&�&)��~g�FI��^v����깍A��r��P��47��R�rK��ёL���#'�����QS���!N�&�e�ѿT����r����5�E�Xϼ�࿼+�Ι��������q����ҝi�<�M	P���4�,��m������*��<'�<%��ܔ���A���uH�����M�9�+�q0eR L��3����L@Ɓ��pu�5&��^���Ե�Dj�RB�1�B���Uiy����X��#G�`!�Q�srR`M*�W��)
6�#�$��
�L�.��6+ф����"��`*ɾj�kSJ���s����忣�E�kR(zm@m
�T������mE�H��ؿ%�����s�u���M/���^S�cBG"Z�7��$X�/w/�&��I�H
*'�
�SR=���|��7�4Y�-=h���w�}"J~��b^��x�]���fZP�%��]�8�	�kOt�����RK�߾�y�oJ�B�\�������ڲ�os���n�����~e(����Y�M��,��`��O4������>��C�2�C��J�A�a��m!��۫9�j�W|��$�5�5�j\�����Ҏ����o��kPs.A �[�e�T �?Į��y�O��E��+��_H�q�3������/ኼ�}�q�%���� Oa`b�o1��&�c��"O<X|bX��?n�u�VYV����4 ��`SQ�'sM71X��f>��6-�+@}��ƅt	�x��ň�g�:�#�<Jf���Py�B���S��[ݖҐ��@��K"۔Ͽ���Fj�$��`���yN�#�z�(���7��T�m���\ڛ�E�����{�D�����U�;���u|���� �~*]�Y��P��3u^ R��fS�VLΎ�.��r�ꦁ��Z��pa�-RP�o��4���̲��R�Nmr�O0b��RhF��?�D���`K>#d�q����PC�qr���J٣@�%�Y�Yy�	�=����.�"��v[��W&��Z6?�����r���)ܖ.K�h̐I�?6p�	=��j9<|�o;U�1�Pq>A�jRƞ��]6A�D�G(����!��o����RE���$h��Ч/�#��F�"�̆֯�u����n�2œ���c�:2!k�ւ���d��hX
�P�D�k����ҩ ���h��h@�\;&KZ�27v{���p�l�'�~>�~x�XT��R��l���VBl�S��@;�c,�E�U�5-��W�ڂ�s��ܖ�jj�z,�;����L+�^ƶS)��L�Jb�N��Ȼ���M
w]��HMѡq��1���s֝e䰑�=-9R��q�m�YWh��8}4�0z#�+�-�u�=ˇu��43ċ��\n�4xm� �-��lG�ud�������aa/��Ê�hsA؋�{=9��g�����������K�x����-%��Q��^�a.�7QS^o�����l�юc�~/e��^g���0�D�D!^�$��8�`ǍE��1�w�#>m��9��C���z�AH0�DLy;��|f�?��-~�L��Y~8�qx��4r����}�8)�sGO}��A�k��E��؟�~�!��:?�ch�PHK%������@ٿʜ���Zs��VH)�����V{U��n��P*M��C���"�+�k� nʩ�V�K��pg��:��,+ e�	x~¹XP�R��[J[c���r��XΤ����:wjc��N9�2x�h��Vz���҇ͣg�пvx�@��-*=�e��Ad��pˠ10ڟO8�t��b�1�};^��������q�
6���9�c�`�T��.E�"�LB��1��h6	�?:��n�q	"�"�C����i��L�Hm9W!��X�BI�b�~J��_3����6���[�m�x�5�=^$������x$$�m0���+%H$�͗M�)�O�}��/L�y������8��f
��oڻ6�'�y&)v��^�WB"�ȗ�P�w�"Op+�XY�Z9�ᥬH�HB	�M�M�pS���C�K��������9'Zm�����x���� ��>�韅F�N��v���,�V�t�	-1ޔ:��e>9���[���WYB���M%�#0Oc�0s�*�9�͉�I(�U�
r��>Z���`���Hۗ�{^ʖi/����c}�F��P郠s�<�~s)�o?m�)i��}���c�%�r�~�#��l���s�Y���,���P��O��/�Y��?��˼q����Ui�<8����<���&�u����F�����(�Ϝ���̾��.��D�i/���� �R!��w��.6�+�5XU�+x�`��}%�$�-����+�L%�?��
D0BD�$A���*�0��|1���p�A�F�y}-�&7�6؆9 ��no�4كyj6�ڛgf! �G_�>���ů��������18�:�sN��/�_��l�m�>���{�*yO��3P�~͝�N͢,�7�n�"	�i}�o�t��ϖ��EOЪ�̒"!oze�1��+����%pg_V��f!0�`���� �"�z�ܔ�A=�,'וgBgE�{��!�zG�p��!����s��jK�/�������x5���3�D��5��H+��8��G1�m0��rRb� ��.��nmZ-]~x�-��5�c1�>���2�zu�t>s}�]�ĵ|E�]Tnk���,_��e�/%C��2wⒾ�SeA;� F���Fz�K
�E�����k�����Sw�y�U����T�"a���ٟ*�4������k׽kS)��V�$L��������ݩT��W��k��6��x��)vU>w�@����(㍄|��c��^3�})�6�I����&������9��W���֨����^D��P�%̲�,=`J�ɋ�<�b1_pG��uD�R��1ͨR���)��mJN�w���}����yq�C60'��<^}?���h�Lٗ�pQh�����I��s����\� S�1�%۽�~��m���6o�tƩ����H�����?D^�A����y6s�<&��>��	`�o-0r�)�rW���ɲҚ�r��X�KH��s[�Ί��b<�}�w���S����cN(w�ܖ������5txe�T�	&��Г*��8���6�Öa�ʿућ�/+� L���+���3x;��U���y��Y��Pݒg�3�a��&��e����
�V�j����^D�-�踍���i^[1�����
���e�%u��y6,�����>/9�*�P
�rj��y�;2��b�l�t�uZ�ԡΙ�2�U���5~�%��6M�.����g�z���e�3�������8�d^�-��0ʞ�v�v��Ip~�/-k���b���"�g���.�~(0��>9딲��y�������1h_1D7
PI��F��"	��M�5�������uLZA�n,��Ǎ]�݈WkՒ`��/0�r���9�ŉb ���Ys�i�)�=���OJ��h�/� ������͠�V�6Q s��`^>�vZ�
�i4��gH��0��udF�f0�Y�9*�oW����&l�q~�m���'�NR%Um%h�e�k�3����J� o�H����p�N�"0��dN�`�A�i���>Fn�˻HwU�J>�m���'��<��C��V�@���FM�=�;��X��J�7�.�h{x��:-5��rp6���̒��A�����N��#ڥ��^J��K�z	��k�bI*x�~G0A��8��Ԑ�Cm_��)�}.�!	�ܶ^��FD�'ıj��z�:�^�ZI ��Ҍ�&;���%�2��N��v�ZFMH��Y��P��m���#���L�5"�O��96����Ehq�朶������m�1    J����l��|����:���h�\�oj^�u��&�/��	���G���.�	eP*�X@��1<�2	�XB��c>
�ʿ�i�0MV<|H~�Q�ס��F�=���LR�i��BR���/�K�����S?�z�Q�ȼ����a�/1�r�y>����?��9����G� �p�'I��9jM[���a�g3�|^��w:d��1t9�.q�pXg�0O�bv�`آ"~�'h��+�p��a��jP�+��=L ��~w�3����:?(�e�Ȇ��vP�9�=��A��l�|�K$�T �A2�9f9�v������U?��m��q�ذ��]�8��t�$Q'��(�hfNu	QT�g"�u����^o���_�uj��.��C�`s��JT�37��ɏoyh哜����I�ϵR@��l��� x2��m�<�v��ُޠl>kϩ�L݈^6���HPƜ��ir�'M�S�4�n6��E�sW�5�
j�����c����z�SLt���݉.N�7�v&�^�RwX#��3�	X_t8��9��8>v�]��s����eG"繾m@�&0b鐝irn�ۃ˜r|�.C���K|lih/��6�R� �'���ĭD�L��*����L�B;��͉�~5��nhwI�N�y�3I��Kɶ��:��ėT��I:)�,GbM�30p�U��}S���F��B��̴�����3qu�סy��yY�����?���A\�/�b��aX],�)S�������N��8g���oԪ��H�|V%�F�m�O�t��#�8Ҟ��:E&ۿn��{2�Z8Q�3ݼ�0̬G���w���.c9��Cӹjd�9n�~U]~���&���$�V�{��i�M�Pc�������l
+dc������Ñ���!�Y��ܠ�&a}�A�/jlL�dȩ�wC�����f�SnysސyHr|A��믣��p��2�6�s7�]�ܶ�LQgV�w}Z�K1�wE��㶼�/y�ZlՋ��HF�Og
�5��?���{���Yl�hj_���,_o�Q:�-�t'5����M�*���S㜐�t:���BGS]��i�>��3�`�f
��Beu���#�f\���eE���/E[g��Y��
�F�d�%�b/C���$�.Ռ3<%Y��&v���4���N*L6[&�7s��>s`�d�Wv ����@�2�N���ȹ�fnS���dzt�N��-�3A&����(ݛR����Nw��S��r)��r,~�p�R"�e��=�>���.�܅A���Yl^9��u^�b��c2q��3�4y�'����S=P���Me�'�ό�$'<������T�Ś��"�a"я��_����aM&��Q�h���ÿ���~	ѳ��>i�*�_��O���nP`��H�ߋpk��jX�lQi�15
�k�����`��K9�4���'�k���t&R���QA-U�J�����aŎY�O`X�m�a�v.*Pc��^5��$��B)ζW�%s��r7��R��󧒪��k!��'n����0�V�y�N�!'�Nx�f>��)Qak�ѦW̆S!s�X�9ص=�#�T��9��UQ�Xs,S�8M������} ���q������a�%�F�vhX��!� K�PV����b还�SDJ�Z�}ⅇ����o�%+��� �1��SBqC��L�r�Hʹ�#�EX�/�n�^�Ë8�j�`_sD��݆���%��_�q����%$N48o���|�<�����E_�q�=`�}8QPx���R~X���9�)쬖�y3l$�����^��A9�AVZ|��,���%I�*�F{k��'c��/�9"uF�EW���/�fW��5_�\1��w'
3��V�hz$�&����s�L�ٲ%�лJL�4��4���BLt��]}%~˛ꧺ��e�N����~��^!dsp�5�׾[��_+��f�J��e41���~�D|�SM��F��6��/[{�ƿD�?�����xO�8�Κ�t�GW>!���b��T���r$�T��JmX����B��$N��`�dͽ�n���E��OXЁ�&«����V.���c.0;)zfa�����{�;ר
Ѫ���������ɀ����[;8$^�����3�H���գ��,��s��aD�����G�~����{u�(E�a�/L�QX�k⌬�@�=g �;�}�XI�h�lz`�T��~�����3�g���+a�§e��i�3d�9���3$���8����*u�ޝ�+��Gw&� ���+�~�AJS��͹��|SL3���
�fǞ��ſ�1���UP��\:�7��Ķ��	/��X����]ץ����}�_�rq�+���+G� �	9�·����x�>I-�^�Ԍ�m�����ENe�ie$C1�}�������,�)b�+jp�3c����"��Au SՊ-?x*q�i��[�;�ˤ��f4̍��A��6�J�������?i!i�ȴ�	DdRM��Z��
I�yՇ�ٶ�"�|q�)R�����Զx�Y�u^-�	���>�d�r���j!�j1�nm��tr=�F�pL�Ij��W�f��M#���Pd���g9��s0�)��@3�|֠J��RJ����	٩L直a�a)+����M�h��%���N;˸�H�w��;���RB*0�b8�@4��M�#8����|���K2�Z�����8�\Zos��ra�&�I�"_�ƛ�}�2n��?������)�/�	�T��&[���O҈e:d/p�6�H�.h�g��f\����6��c����Z�ͩb�8-�F)�1�RH�;�B@y��c��/h{�bX�Oa�+F#���+M#x�1-�6r�s^]�d)�r�����I�v�*,����{�w�{H�����;��Ν�I`kOC���b�z��a
҂\K#F�.�9ozS4�aEKtl�G=�?���d�t�����������BW�L�����d�U��?��㣙*����Z~�E%�6%p�mZ�*�}�/������0��/y�A���%xT��L�c�%�1Q4xv�Wf�a���N�Ͽci�_�d��I���׾����'������.�JՄ��f��S?XMR�����-�d�J�� �R�����R^�N��V7V�'�V���y�z>>A���H�:��X��,1E!h4|�_h��R��R|[������u���ly�ߺO������M%�3��Q�?�z��0#Јڒ���9��mx?�y�7����V`�*���(6c楨h�m���ߺj���6�N>%����KzK4P¯��
$(-�嬇'SC|:y),x�; 4o��,��/���ʋ��H�f�G1�K��?s1����]�>1�bzb5{����)�6���fxeI<���trf���I��˂�t�r��[H�YdCR�r��D�";��!Qn<)/wL<�뤛;Q��su}�~�<�Ҩ��@����i�~�r��xOK�{�,j��(7�*P[��T��XBՁ?A���;rX��R�O2�����?��ָ����m��?ΝXN3jί�����)o��,M�ą䷙._��f���8R�����`�,��>ْ�gܴ��臭4̎�ST^��m�<F�
����pұ��oL��-Z0sy���>�l�j�b傍|u����T�0��5�I�^}m�>��̴�.B��C�^���:������l�4�IN>�+S�%0&�%d�yqk_��$��6[V�49�iti_e�g�C;�%Ё�B��^[_z<e����ֆ�B����kW�}�{ѱ�����MٰLL�`�pHZ���8-v��,{�&���3_/���n��&�m$ᑉ~�X��t���}���~�[L�ã��|/�)fcY���E��Ȱ���Q�I%���~vo��H��N�")oNǇ���G�Z�K_�l/&���*�ZXk�׿�2,!�
�$_6�#���!$�3���;ob(rHT�>]K;�i�=�D�7%CX�ݦ��mI/��\��q� DR�{�LS],�%A���E{cP�fS��+�]S2�    "�|ƼRE{�= �K<ʌEh�'0�'�%�/�;s��q_�@4�O�q������so��rU����驶$z�"��
Ӊ�t ���	RV��N�(NͲ��f[��X��rJ��iR<̉�:xì�����|���.�+#k�sLJ��x&P��w��W`�5��Ǝ���湊.���Y�'�N�R�L�I[�C|O`N"K�n+?&/k	To�w,e�x^Z��+�Y����Uh�j���� -[t���I�ot��'�Ǆ��*��Ĺ���~wD�� ve7�Q*y؃�k�$�j���Z�f����\,�&zL�?��G�p�)Zj|���}��]6�>+&�O��3U�����h�rO���L|3�ٶ�J^����^����ӧ@���\#ǥ�	LXu�	����$Qp�_��@s��j^ �5-�y��d��K�;�È�^A>�#i9���\CY�%�|k�;	��c��H,\���;���Q�{4���G������TC"�:`Jՙ�ȫ��ۄ F�f�L����1uy�-��C81G�,��\��]�������zRvR�	ţA�\m���/ϑ��Ι��hU� G*����r��;x����w؉M��$g7�q9��;��h�T:�.z�s�{��b�C%��Mj;�峬�7�)�	���?�*�u٢��;cF�%��=�!������`���5��9���I�6�r������^��4yR�7��:�s���eg�XC��ϗ՗�\W�r�e5o��zW`'�r��������9K���?�L?���?~�~�_�ɬT����k��̄�H�@r��ʧ�2��)�R�ӱN�1=㪗,�P��O����Dk@S~�w�Vj���hx��CJ�٧k�ʞ�j�4P綒�]A��ܒ�0�-�t �*�~"���^�{
~x�����<��y�K�W�x�z_��ˠ�8_�1�&�f���n4��Hf�J�HeV�Ǻ1�ҭ��$��]�Y5f�qر���fo�:��O�=��l_�_�X�r{g�8a���+�l��X��|Zj�(qOg���ݾ���>!]�iT���`	��٥������S���8w��D?�Rf��*j5�)V��qt����X�������W�m�p�J?<xA�*�3>���:����3�zK��[�/�_J�:>�_��y�}�����a�C݊�*[��=E��}`}�����!P�L��2�>��=�<y�/_��Mfĕ��.F�C�&I�Yu�����W�t��.�����p��V�l~�E��t�9�BKPB�݂�ۦD��/�H2�I����A!(Jg@���g��1A�)�0���>?y�� �h�i�2��r���r�˩!���Lc>�I�/����gA!u��I�ü|� y�$Ɓ�̳�T�@�\Ru�E��F�Q�r��}�<��K$B��r�W��Yx�� �o����#���8�D�m=�t|I���H���r����ˬ�ЈN�Q�����?���<$�<%��0���Pퟏ�h��Z����=lY�+���WN��G	�)��Б[�Ҡ��T��a������%ڵ�,׺���w��E,K��XƊܔҴ?5h���C�۫�����v��v*�3�![��Z�ǘ��,e����4g4�{j��N<k�iHN`nS�u_I~��bw�E��-�S��K}(�x���K�##><�}A�9휳�թ~y�4LN�ṄpҪU�S���t�����"9	n�=3P�ђ��5II��>р�A�f}#�����!a�rH��|&�e�Tn4���:ѩ;�������Zcu�5��d����m [�]�W\^�Y��I�i�iڙ���>�T팟�/ys�m�>��ι,9�I2���)���,.��#��_�&���-��6��U�JX{aW��@��f{�<`��`F��(�#v���+��L`}{�eS�����C(n@�Ȍ�����h[=�1(w\�j�����shfww���/� s�J�P�����`s3x�Ԇӑ�%l(Yܲ|��\"��g����@N���WixO���"t�NC������	*ٳ]+��fR���o����ߛ���s��#�����.#�@���$����;`|Z֤��ٞOpI m��hк�rNS�0��aoK���"��m��s�8���B��%_==~nQ�s@*�/�n�ѫziQ���,AŶ2�&}sa���_�k�Lr�Y�(���M��d[�w{�z�.���5�Eɳ^��$�6���'�9^��Y
@Bt��f�Dg�^�1���ӆϙS8���=b}>����-��gWg�~S	�Y�lNд�o����[[?}:���������*:@v�~��?���)�u5DzW-G����+rٙ%�q���K2�~�
�R�c���zVn��'3O2G?�?���y\�s���3�b4�U{��(�k��{����z��MŊV�j
(���`]��gEC��T2��0��lm�j� ��<˳�_b1=4ć�b�^�Ns0�$��=�÷;rn~NWAӫQmND$;a$��߽9�p���}%!�@�ws��t:˹��h����M,���dv�~��RL�[r�č�]H��t(��`�L�94��Vv����B4�r����6�>`�\�����4�|��)�Zn�åJZ�z���KX��4�Ӓ�ʛgG��
%b�Sw�Lf5���K�Y�.��zc�k ��Whya�h޼=�)W�&���U�����k��D>�Ag���I8ZY�
R�3���b����1Y�SC䂬�������kI�;G�n�pr&fR���E�#�k��݆8y��ȫ(�t#WҞf���ْ%�O0^�J�6X ��F&�t6~��u��r�R��դ
OFy����hv�8�[hF�go9()�������]x���Ed���ՓXv��9s�[dѽ��#�-	����y���T��M�+U֗9�lM�ܞP�wQA�y��.'�W�n�=Ԟ���D�|L��ӓ�X^�J.F.�ߩ�&�N�Ux-�xŚ/�Y�b����K��߮6H�z�?�.��`�^=C��,��yK,���"ʹ��ɹY�\�
�<_4�5cBB�μ���M�c ���w�~��l��$̬}��؇j��ك�����oB������yu��gL&5uw�؏��:MD�rYPt�Q���A	%=n�:�9g�Kɚ���0l9H3��ɜ�y�@�
ɘ3}�~щ&��S�O5�'��A����I�k*�L~�_�t�˪ű��k���ꠟ1E�b��{5�������L��ދY������O����^j֠�2�#�<�k��eMI%�X'��7SPñ��j���u[uw��,�;��6D7��E���sM��KԼ7m��y�y.��z�繫�|u�O�a�~����$E�����_>��؊�?/���s-u>"��Z�V��6`']~�=P��������S�6X&�.���9�4v�E�?�w�t� '�� ާ�vJ*�y�E�))
~�1�Z�) �@˜��i�[Ǥ|�W����PM(ֆH��U�<H�wJD���]Q��������R�2GN��i.�B9�O�W7�66Լ��nJ��X(�`�����0ͳ��z�&�¾Q��,�C���'���h�����\�X�5�t��r�J����I<أ�INB�=��$�3q�hU�>I)�b]ٶ$k�T��a����\/�kT{�s�-�'��q��{��̵�-q�C����Ե(�����:Β�+|���}��YѨhs�B@2��!��dР��RR}U�u��y�;����A˒Rܧ��,��ͻ��4���[s��7�V�i�1O��dn����'��H�]^��n��DҐ���ճ��9�6�*�Vb;:>y�&#ֻ��0�˕�"*ϋ�r�T#�z���M�!�7A�LK2Huy�TW��u@�g����Tv-����o��K�`��U�1�/��I�O��W�% }��H钰�u��ɂ�2�_|T�fG�&r�q@^/�/���5�!�vd���&K�*���s���9r��fy�r�E���-�    �e������,099��}C��wN&>�����!>��)"hE-�2�X����h��4��T�&ڰ�G޽�5{�60��-d�r��fmƇ���:h$t)̎��ǎ�0�c7mp��Ʀ7T���ɔ�
��W��\J����� �ۯRlE@�:q�\�lkK�.a(y����\���5�|��US�Q��~�3x9����UR�0�o`����y!������cF�{s~�3�[�Z#� <��)ڞ~�%���0�UG�#��aw���Α�V�dK�n�kԠ��_?r
F#w�E�����F����i4�,>" 0��ɫK^"��=�{mXt6q�j�$O5���G��UFwf��4͐g�E̍,����)f��t�/����Ѫ!��5�`��*C��R���)>����ğ�dp�R��@���8N����0ř��C��z�XNZ���T�4��9P��I��O�ד�α�}�J~��g�$���tK��W9Z�A�U�F�����QQ�4�A�u?�5i�$�'�#d0֒(�Oј{�֝�w|�?��˒���0>9g��k��$G&p@�I`��f9��k��ʀ� ������2�����_vޓ��\�_ښ�)��1�W�#��{����$��Z
K\�g
D\���A���U��,mF���E���o���j�ȭ�c��ȱ7h"a{��;��Pb�fn���/����T�ؘ����ؾh�$�0�r�D��Ty�;
�=%I_\����1�;�/�؉	HO���L��=��|0d*�2�>��}��jGˎ��{G:5�[��aS.&&N}Nz���ڂ���8�BR[�v��>��8��� ���u��ٰ���Q"Y5�ll�V�Ď�"��}�̜�ƺ<�H���|e��912��rjNO&��%�G\']*ei턱D�^��p��W�[N?�"Q���S��	c
�Tt�y{�X�5���+xd Oc
�(�'c#\��Z�쯯w�C�b�Z�$�)x-R	�	�7�dko?k�%��6���_00���_��{�&@P�7���i����9C�Ǟ�M[��6V��H�	�{O.>�=	`D\:��"��}��P��睻�wz�K�c	6]wJ�	Jp�p2y�W	��rBҦ�N{���/���[��8�&����܍�j���-5M2פ��W�6~;�����#'��㌥c�O�y����w��Zn��|�Hg�F�������LY���Ґd0mZf#e��ΰ����t����E5��,ՃnV��v<�� ����䍶��d2�*�S�H��TnM#�΃�L�����T�����[�֌Z���� �
 ���Ͳ&Me3��&�x�RE�5�<J�_e������Ї�`p��w"��2��^'�y��yt	��?���϶�p�1�$[g�s�T҄�˭zJ�ř�@x)�\���3t�3����紬U3.�0��R�� ��ǅ^6'`u�Lx�������>�+6�n��[pZ�h>a�1<�)�7UE�NԻax;0��G~L)͸e)�7��|��[s��r"��߻��d��X�]��7�W��EX�M*T�r���*d.��`5�����me���_���yJ�e#~��hoyxҢ��m��f�e2�����H� �'m4��U
�G�i� �����9]�7p�1��r��`�ݚϤmn�,�,�֘��=�ssB���_����"*85�	��%�#h��p��sR�Y�-7��
�zw:�e��ӿҠ��X�j���G=���ᱟJ�czEE�eb�s1d)I7�hpm于��eg���(W�'���nEk��l!`-���~�ENn�FQ�W�/?*��H���4�l2D�Y���eu��8I�M�D�+-�.Y��> E4O��'��S���bDڇ�ʝ4�3��'h��s�T���{y;K�s���Y�aj1)���%M�Sp�8)���訯�]��h	*eX2��~�x�f���S�l��$�@�(A)�򩈈�d��'O�5\
bi����R$��$�8���ѼT��,<��϶)L��Źu6����P,�P�!I�R�����.Q��\7���֖?��h�y�Ҽ�'���gH:�]@K������uQy���zm��]!�<��\	�XS�4�	���Tݱ�+�YR�]�9���<�Q$qa�-��ég��Rی�taKmÛY����;@�4!���X�z�J��KGv?���b��?W�6J�=&Wnt��&�y��`�b�ƹ�3���_��7�q��TP��'���2%���I��a�.[n��M���M�2�$��+��Ț��Y��4E��vӸ��fr>;1�\��G�>�lA䟗W�ċ+���7�;%7��&\�kek.����i��L$�هM&�S'��:Ƽ"d�� Su.�Pg�UxO-N%�iTR�P2�v��m���#R��I�Bב��u9�qs)���yXV�Mh\�l-�.�Fk}�"�o؁y(��h�.�a��d��������ns�wsa,�rQ.k=������]�|�k���i@��M��f�/g{h��j{.����CSP�P��U� :��Rqh���\$�D㊕9%堟����[R����&�9��Z����.Y]�������Y���v$�O������֧�%����	��._��3'�iØ3e�@-&�E�Nl~K�3'�.�$��F[ޤ}�u.5�'(.�?�����+��4�C��<�[��TK5����9���
� �s�Z����Uֿ����yj���h̞ ǉ�xc�K���St,���M�ayr{=/$�y��\I�������b{���f��G��K�w��}1�#�d�C~��?{"�~�u|ـ���'��O��A�E�v�wJ�T��Y��*\�ʒ6U�F�������C4��o��<v��n��O�|u��c�\�`;��X:��I�b�b̙"#��Lq���3_��.�H�� L�1����8��uU�lƽG��F���^_2������l�;�Dɱ���q+[�<c����f
K��� c�[�|2p@"яk%"����110x�-T�^�`)��iZ{�3��Sz���1P�Yf�n�D���IO�'W�!뗛��7��а�� JU:}s6)�$��wJ��vYX�b���x4��SP�OR�﫭S�ۉn�d�a*
��9V��:q�Q���uK���:�%�=y�9���;��k?d���d0$�a2N�=�l�H�m+����x'��\'`Q8�f:0�MtI��xlq��.�CJ��U�Y3���T��>����"�U�h3�2�`}����M�0�rN8s�'�%��.V$�<��S��w7�%HߩS�f������A��	o�b�L c��b��~rS+3DkN�Q>f�P�i���R�=�b,NpJ���Ä�����d���DS��@xT�j�FG�22�˒6�������&��p
�D��v�O5͖�G,ݔGtۖds��o���ʻ~[j���1�#�.I-��O��(�>FrT
1��X��BFyR�&��e���T2M%���P����'x���c__�e3
��ʃ�ظ��
t9A:.ߍqֳo-��|�9�	ܼRy��$�2�&Y�v��z�5����$DT��o`��q)
�Ե����e��rt�u�^I��#ח'(	�G��`�|�ݎ�or�-�c� �0���RV��h��b��l�W�ƺ]O�jƽ\KS�(�ǫ|,�ޖ�Z+�3���y�<�j�Ŀ�0�����a@]YN勤�����R�M#��]H'ȶ�Jo&�iq�4מ|�3��|@�t[0�Cx-�b����2��SG2^���T[_ƨltȒS�|���푶�e� H1�2�e��xʬT��:�'k���ϕ�9!%��F/�4�'>�fx#R�_<�=���)L�zz.�(/ࡎ�� �w�Ƥ�^e��H��e�:�䄞��:���g��rR7���S
$|�U.���dbA���iU������.�|��?���N֩�0_u�U��'Sn��<��QA��`z��������S[8������Z'R��FJq����G�N�����J�����GZk4/���_��[    -���4Q���Pܩ��:� ����(J�ǈ3���ӓH�1�~�ё����.�^��-`x�oo�~�0���_��N��e;tk��X�[ϘL)��f�����v������?GN�Dj�O|���g���P��{�%_�*&��[d�B�p��n_�rX�$�a+���DK.$N��Z���n�fU�mo�����ܗ_�Sr�t��k�a�"0��9΍5��ǍCs�Y�.s�	����O�ZxN�Y1�F�:�U���0�R��=kL�@������;_�,�8���}��+b��ɤ�V��l�jђZ%�Ka2��a.;j��)r�hʺ���9�o�gp!�l;�b� �]#6̒2BФ��- �DEM<�0�����ߔM�[�>�����l� �]=��|H�XK�{�%	��t�9n���*9�V|���)�S\���%�e/��~����a��3�w�����w�͇e]a�V�C�TT��ۙ[U�V�Q��K�Jm����g��?MJ]�A)E-����A�_4�����0�o�s0�D��R�1 n��{�^;�<��!�����š��BT�JR!�Zֽ�rX�H�p�U����t��\oK���c��S��4����`�͑S����y��`:��Sۣ��g籑�zm�פ��ЕM`��k�����	�D~R����Ʋ8x|�$24�0g۩��)n��%��)�%�d%�PȞ_d\�WnGv�'kj�%�TGO�g�y@VB���0�<��g�sҌ���T��zip�9>)Vs��\�	����N�ᔫ(���.�$J�(�<���AI��}ժ�i�tP��j�JuԮ��� &q��R��UÒ ��N��O�gj�<�6�����,5��N�� �-$uv�>S�I���}&��4��[�,)Q)$�x�#	��쎁�_��F?R��B�<�L��Of�jӀ{����fP������o����C�]qL�q*U�%B��s�u������5֍���Y���o�d��sTfE��K��g iH���5�1]��_�})��+��d��(Չ����oO)�N��I����u����D��m�3���x?�ZM.�Xk�:g) �"ca�bz�L��z�k�������x��^Ɏ)d�����=��O�7���<C����䬣��YΙN��b�]q���Q��1x�����%���ߍt����%��d�u�޸e��w2�Jp:��Z�W&�4q����92J�]�k���T�#N�V��	y/�>�Թ�����S1\)�"ה�.󄑭v�19۞��dZ�tZ
��+��'�H?� 9��'q���<��}*uOK�9o���s"�|s��|�ܲ|����`}Q Vr��s�LtK�kKx��+�5���\��9�G�m's{�������|03R�l����M,�.��k�˸.�u�);�5���i���C��o���[��>�/����'D���Ny������)E|�m`���A)�^���s�vźpħ�e�\�q[j��V�#�����Z��[֭��s���s�9�'�Y#�˚�6�SuTuP�J��c�|_x��I�%��pS ��وQ��Z��!�A]~�K	mϥn��$c]ӭO8��P��yX[t��^�M���yC�A��g����L��Ǹ7�c�;���h�;I�
��1�G8��B��)���@�BaBO��0Q��@8�,jcfY��	ϻM��Ȇ;��t+�I���6U�Sb<�a/���`g֌A��s�uδ�����}��C�ב.��5A���C+�̀Z|$��q��O��J�YC����8Kr	#����m�߳T��O�ijϕP�2�U�����rϴ�J3 q)��Brȕ�)���H/���v�������������]E?K�����, j0���/tA<�g^V�ᤢ��q��Km��5v����,�goe�<��iy�)�i���y�0@S�'�y��D[ ;��@"y Q��Um�s&
f%�޺ õ\Fݐ�5݂3��4w�u# 81���*g�qs� e�,Ēg���~���8l�<�����ڕ~nK+�d���xޏm�l��6�x��&_��Z���\z��@&d5�B�q6'bv��i����~��.�њP��v"�T�M�M�eLf���w�<���	@<�����;����;%Ck�p��u�E4ׯ�Jf2��=�춧�ɍQ��X>9�\�� uq֫/�2�5��%��r9�)�Ԥ��|n�H��wz|��i��z��H���lk15��.:A�����v���i�� �t�"H{ӿQPHo=�Ex� �A	Ҥ��h)CT�����:�`���� m<� �a�n
]c� ��!��ZF7y��l7s㺸������L� ��E
]ҋ&���qC���5�K�N6�����У�q�Y(�\�S���<�<`��3�9� u?�XC�2�X�N+��=��\W����Ӗ[��m��\5��Ϛj����Sn�$�^:Y��+����#g.h��N�1��L�O���T���ǧ�H�'��am3�!��	�N��;ʡ�� xq�X��ע��m�۾�Qx*N~��i%��)�JJ��'v3��Ҧ6��Qz�u�=��Y�s8_�<qaІ����gH{�$��{:��4�h�A�%j���;�ܸq�4� ԍ��N� Tx9O�Z[��������0�i2rT���fC���k���oO�����yk@�d�1ύ�g����ء�E�|G�:S�w����=�/D���v�䴧ZZ�
߲�cGW$uuB���G�(c�m?�?�}R������åq����m)#��y����&�UP��9�����X��k����R�A��2��U��#;��NO}�R��L��\>+�ɟ��~��cmqi���n:����c�^P��%���&)��~�@��>��Y{�ӐVs�!���RH�I�I�Lm�}J�h}6�m���;�s�r�ܾb���\�)� ��I�Y���}��7;�̞� �|\6ne�j��[��HWg��/��e�:��ȿqږrr���jM�<�fq��gK8�&��MB����أe35n}JG�����"Lힴp#u �uQJ5���j�-K�}�L">//A������¼~"�k>R���x>'�=�kZ��*av�j�a�TX��	-���RE�6b��3=����<��0�"h�>P���Jd\��w(T0�3W��Kk����.��<�T��Z?��Y���J���nJ�u*X|B2������
K�X~¯m�5}�
xX���kX����s��v�8j�ө r���l6�!<)�k��p0ǭ_s�8���@�̍��ŜM�=�E��Fɯt�o�^��x�`%��?}_f�|X`�|��F*Y�Sa�t�z�1�S^�Vf���t��i#�&���ï����8��=&�d�P�����&��g(E���'�n������c��H�{*��"���v}����cg^�a��U�`x�	te�GKt��Qbb�1���L�Ml2/��/�J?5γ47�r��o���uNQ�����9y���L5�Y�Y��$�R�s�J�~���U����m�T@_���7��X�2خ�&��%�=�Ȍ���X��ŉ�*��:��I�t1w2����?�J���8A��v��hYIJT#H�qmq���e�`�}��8j�?�Ȯ�c����l�/�d��lts��c�\���v��/U-)��;bZg��*t�0c�@�A�'�)�K���:�!��Fű�^�դ�;��UkC�t3rBr6���ג3=$��i���fb��=|�9N�4T6�Q��X��dbG���s�OB�pK��X���dR���<���u�2[��Ǒ
ɱ�۸��RH1VHJjN%�]$��1N4Nk�r���ǝ�����5��u&	9j��~s�r�(����S�����$r�v@��Υ_���|�BЫu7�p
�+�S^[�'qc����%�@9��{a�eWI    u̇O����ى'W��sKY���߼�U3sp4]
�n����ty�e��MA�_i"��0}���ȉ��t D�ҫ�=�sQ'b1������?��j���8���nD%Z��ܱ������K\�\Ȍ+�f��LN͊�D��z��O�N�:�O��{b�J��}K��ff,D�pƹ")w�F�:/(mkjӕ�Oj�	JN�5����53���~'=� �G�%&6.�4ch�$��*;,{i��2���Ӆ�����LrZI7�&��|��Z:1u%�i���{��<�Y8);y�j�H�+��u��i�>�4Y#;3h�B;�o)+��qi����s�O��A ��"�1��7���\����D�i)�+��~�����b�|顒�]�D �E�c/��<|{��w�-��q�AX��)���6έ�R�*�%��}���em�)?%�qO�P����Ќ�a2� 8\��k�*2VG����U��Q���_��S����q�U�A�L��?:�~��)}o#���f���i���>��o������G������u���m������A�7iz~EXJ�m����9I'�`��l	@����~-+�˥}�*��N}��4����Ӌ���{�j;���e��&�eb�U֯	kca˂��z�dh���lGو@&�2��LJ�)�w�%[Kck�R�/��S��J]�>ԂA����І�ô8>�Pbߍ�_�8Vi�����.G�<'�c��p��Ѝ���Q>�M�1M�*��o�'��� Ҽ�4�)}��s�%������iOPs�r���ޭ�	&9�&�a���zE+���,�̕/K�4e�h��m�o$ɩ�RH�_lq���R�K{�Q5Wf��i0]�`��܂�H��T�[;��-K�I/zI�8i<ܓ�ҟ����R� �F��I��K0.iǱ�C�\c�%̀��њ���QR�jX���h��Ql52���T��Ҫh�z!�<�Ԑ-E���ht�W�-�'�7�I$$l��R��z���T���I��Ԯ��Vcc&،�~�mCs��bBM�ӎ�%1EV�id�N�h,g�h��R��K[~�Ex���!A�'�-�B�ja\��f��ƈjhC�V&5�s�Z��W8�d-�H���~)K&(l��8i���izq�Asl��I���#����B]���3S���mRA,<��a]����@?/!�Ɨ�Ô�?�di���v�hj=��CR�v��*!(-y9��Tr�w��}��I��0���4��v�g
��W�n���T#�v�219�Ol�οy��7�KuV�,��ԫi�'��9���k^b*��Se͸��1n���(�ʘ��X%���k#���}|r��4��������^��@�Ԟ��j��}.	k�MCk��Z��j�4B��`�aL�Z��>�]/\�Kś���A�p�M���TAיf�ç�I��x\�Y;fx�zB�u�Ϳ	�ka�7}���~&����i�+#��~�ԴqGuө%�RO��/Ҕ�D4������iQ�I��J�ʒd�k�p�����P9��+��	��Tn�|�+/`��?��F��B�=��p�D+�TY�9��Sj�S��'_V��bŪL�l�mՄϿ��ӧL}"�~�P�Vz�#�\��V��d_ʮ����]@��ZtE�b�NT��Vm���"E�!�:.?��[�c)ڌD�)�3����X(�b]Z�tH�x����u2��I�RJ�;)/>[le�6�([�q�����1��Zគ{��h8o"�n�f��f����$]w�T���Ob��N1YB���/�+4���͎��-r�(L�f��N�I�A���8��ܜ�r��=��s�O�"IZ���Ih�o^𑳨<0�f��`����c�Q��'��T�o�V�ͺ('�}�s/~z�R���� v�������'��2)���҂jd,����UB�1'i���I��c���c�d�2��[vK,Ic<�Z�~/�>5-#�"Ŀ���;B�]ó<���$qx|zЗ3��q�e��\ղ���\��4x>�,�] �|����O�&bo%�׹p%�0�5��U.�� �!���u�o�7V1p�m���(�,���Sr?�|��߉�SbEB�7쪺��XB����I�&��l&�"я�K&�S	9�c55���'8�4��
���=�pI?kM@���ӕ����z/%��VSW6!�]��Zݽނ9�@�mID��v�L?�[t��!V�ڳD��R��(��Չ�S����	7�5������e��$�_��~N����r0�ƾ��G��9��W_.�X�b.����Gi�)��r��*��}� �3@4{��宴y��j�C���� ������V�	���������6���%<dB�ʾ���(w�O7>XP�9�K��ڧ�@
�7!�,<Sܩv�u����k�	���]��؟�5@^RI��Պ���&�y�
�?��<��C���4�i��[a��~�&V�}���7� �^��Ā���4~w
O��Aq����U��D�1Y���ᥘ���Ln���ن�o�1�^ ${Z��w��0 ��,�u�������P�P��,��;���Ř�؟�y��(u��N��W�?��*?Ӌf��H��l�fҽ�/���ۼrK��49��[L�����'��R5��Hc<�e�ޥcH{�Ġ�:bGhz��I����2�y�����`|^��3��F���3p	� �s���s�\�I��GJ�2�칕>�n~6.&`y90l�.�/��9'�"���=[)/G���%_|�\ZŐ�mS/�0��x���P��'��l�͜�`M�|؎�W�j;��/Չ/:hJ$C�0��(�������L-(
_��lMÚF�U~�Z�E���]!��<w=5��E��V�MU�Ӣ	��"���a���ؼ��� OG5[���0K���RW>ۣ<�mwP�c�:/�	�eɟ��p��wi�,�I�����d?R<�B��2�Ys �G���[���o�:v�!n��gy���Zy��̧����%u-�.���j�r_ә�h�����x�W
���R�>%��B���K�oN��/�IQ�����C^��<\��{Ú�:[qΨ����/�e�+J�DaH���g"� �b��ܡ聞K.;�':�	�!̫H�����j��ӏ�5iH8�6ي;f����P�����1��SR
U��e�6A=.��c�mI�ns��	���.n 7�v�A?�AZ��o�rq�t�O��ֿ.$O�'�ro!���"oϯ�M��B$�|}&��D��Dyk���lۘ01_s���J�:�g���b��-/�%�:���>��Z,��=���Bsn�HJ(�ˑ�*�4���׉��̗(k�n:�6���;�}J]F�
���M�:�h#����*�Z�԰ǇsM��`�xv\����+��%R�yX�ciJ�4
ާ͹�y�)���W�����m&��<�?o#w����o�=�� �l$�r���M`�ڊ�MR�&��x��������d�| ��-/w���;��n�K!~- [N����\|έLW�AW�k���U4�/V��\��b���[��ϒs�H�i���|z���Ю��m�N��|G}��s��0i1@`��2�#��nQ�L�X���Lo�&�sbG[�Z���HI�N?:��|��^c�t�r����`.�Ǜ�}rh.��$z^�[j]4�GUW���md�R�rKzq���ԧϐ�g�@&���8����3�J����]��h� ���@�[���#;�Y(I&�cm7�A�������s������ǀ�k�04,���1We��ڇ3+Ară�+n��I���kZ�$Ji��ȝc��i*S����Pz�h���6$K��z�O�T��P��Mq��5��p��x.Q~�P�.g�C�DIIǘ��,+�ރӞ�^뺡��H�	�-�r�6���������W͂N�<�KK�v�DI��("��`��ƭ|2F�T�?4# �)$�N��"E�O�m�^DRk1    =.X���Ȳ�D.b��z"��zS*ӂNvo�N/�`�COޖ3��� �u?��t��!�|O/~�fj3<����'y��wfKᮚ�j�7�e�IB���ֶ4��m�{Yv��T��=o��I-�H�ݙ�!�����Y�4ҿ��tR�bd�}K��%K��L�����.U��\��|1��nEM�?�7�A?��֮"�uw;���W����;������j�Gf�8�?�E�k�"`fΩ�xUZcS��o�2�������o�}�����z	�m�m����b�S���C��s�(�hD�ō����g{���cqO\?��&FL@��9ÿp��ηN�W;J~�[Kn֒�{��\7�_��dB~}l���!s�xa9��U.Ȼ��Ŵ��R�ݬm^2E�c�[�/�BP��ԅ�fzD�,��e�wL�kҗ� �Ⱥ��Z���bj�D�$�c �cZ��m{�5a����4��i��Q|2wU��j1�=9=eI����FGae�FÌ�m"��KO㖧_�I�9d�$��gXu�Oɜ"���~�U���� ���w��V����r|x��LN��hw�7���7���a���s���~���:��\⼘|O;��U���PdN���3�ؓ��9�o���/�R�Bu���S4=�{V�����El��)߹�F���ȷS/T�������.�Lo���d�?܏��mBȫˤ��nf�?A�~}���)*& �ԝ�ιZBD�zgB��ڹ��n���!���hfb9%]�Jio�K���� �K�a'�� �n�J8�*����{�p%�,���U�$�CQɶ6��`5����aӵM�K0��r�s~&����.� ��� �M#j`�5"��P�CN��6��r �Q�B�����h�L�+�\��g$��?nLMtCY�"%k/�B�͂�$Jj:���<�):G��?��-!������Ļ����w4���Rp�	[���H1���C�=Q~;5�g���� �Y�EԧdDN��$ُɈaZ�ehF\cs�"�Ki.�i�VKu���C�>�yM�>��{ �J~-���_OgO`�(�8ig��T���G�RU��Es�����*����o=���
�}�\t�� 1�����*�ܕm�[��$A��aӧ�}ҫ�珞��=�_��+�xe}:����+q]+)i=ۖ4|K�s1����g�t�7=�q;)�ލ"�R��+f'�ʉ�0�j�=�6�N��῏#��I@c���|�~"�2LE�CD=sNz�g�$�FH�,�Z]�t�$3�}�$NST���Tau��Ck �����P��<ְ��N���'?�Y󱷧ĹڔG��$>㉩�Q�r���z�{���Ց���HDm�hh@��B���$���jI
IC�l�������b�¯��o%R�N,9�$��zYw1M�(��r�������,�Ӳ�"���G�INWy�i�O]�S��h#�4��Q>�u���r��K`�s��mK�S�2݇�9�%�ՀA˾oMyV�!��_��TY����(���܅��S]���yd��A�zysЕw.�Hd�RG&���{!]���rYr疱@݈1�'U��#]Bn�����:��d'��ڇ�;1�:
g�Ų���Ȯ"�-/�+��e���r���
%}_49B�����~�֤dOu�hu�dzg����A�Z�z�	X�<��,��
|M��K���|Z�ʋ����>�x�h)����/�J1d�����H������)���iL�A�ý�Տu��0g�I��O�3�?I7C�¼�%��V,._ky���,a�t#i���4�J��,oa�������&�e$p�q-s�r�T���J�}eɐƘ�}Zʴ/ �r�G�,�D��1W]7
l� �p�=�[���dRW��r/���'��\ ,,*�Oy��#��L�1~l����g��gT��� N/���4=�������yY+ա�?��#q�t��j�^�L�D�0�9��3���F:Ȏ=/�U�L�#�Q���K��O��d� �^���t��x�VI��"�(OZI�lu1dc2�ɸĬ+�'�S��`�5�`�1M,)�x#zi>��n~��m+�p7�R�fq)����ăa�(Hu:��^y\b������R�i��ʙ�L�5}�ueN����3έ(��C�[�ۖ�"��xs�ۄ�1�#���6��F\D��٫&���3��3g��O�U��TΦԎ���]#������)%�1Q2cœ"�`��b.��]!�^;�R(��� 9F@f[����b,S�uj���I����'"�5H,6�k�?1��T�J�����`m����*t�U:���}�`$�5��Ҥɰ����/��e�q��f�z.����D��3,���*M����Iaܩf|�e��������?���y|��Ꙟ���Dh��V�9�/���Ώ�r�6�'�K��M��GFa�0�;��]����_0���h�1������S��m~ӹ�D�t�@�g~)UGh��QH-V��# ��j#�3P��(4^�o���
� ɬ����W�3�'�����������2gϝ.�&ǂ}~���&���x��|�fxm��jN1��q
 [����\2tC��	9�� "��۝����ѧ,^�#D[뚎��K��K~�� m�� ?1 s��-ԋ���i���y��I%�s"�jT>O�;7��t�������=����(^*�R�-�;����4��q[0Ξ4D��{�]���g�ɟ��8���<�T��mn��y�	��V?Yy�
`��i���F�T��fK�lT�lQ���$[�Ѿ��&^k���͒�:�Ʉd:�EcI}K�;Cʕ�xթt�T`&�������6�+�n�x�4��~Ƅv#�|�����@<C�#�@��MG�0m�� �N�g?N+����&� l�s4��[�����q0�d�L�^xV���f��O*��������'������_3�T�k�)e���A1�RAӻ�ܿ��M;�U��Xᠽ�~
�9I�)��Y�+oB������BD���$�<���f���@�6C�g8Lr���]7�M6�]����ς9l�����eU9��ƾ�g<�74�K��ä���r�6z��o� ґ���м�5��ϑs���e7�Ðd�)�����6}lN�ןY闀7�֘��c��	���:��k�b���uR.	������(2��������.'�]�o�#�o?�6+0t��.e���"�CNOr��)�nb%U��_p�+4�קC/3+&L3W۝�U�Ƅ1nǗlyn�N���#��E�4=���%_�:�ڟ��
�t2�'��������p1Ȫ�����6��d1%�������ږ|�T�t�`xpl5vei	Y�%aјpuXaZil���l(��&��7���މ�I{���e���rٱ�åOӚߔb��� L�8=�H�\{�.���P�䪀���O`�����������a)�<,J�� �Mj����hwc����@��g:s��T����� ��B5�C���<���Ĝ�J.b��#4�$Q)������iv�-u�1���{yc3�#P�z����g��}z�
�i�lf�W<�f=��n��
�4�4�S�>,0���#��a8{��9_+^c��[���2"���$)]'D��x�&���"�3��J�S���5w�~5�@<4����Tb�ެq�mӅ�����)@���7��fA��$�x�~��S40���8��L�Ɖ�r����,!Y��6ӓ^�t�v�2)�(En��O��D�L��ϒ�Ǭ����_�b��<ڷ���r��4P��Jギ�I���ҵa7�*ɾ%	*�]��*XJ�iH�6C[i�ы��1=$��]�Ѣ��u�)��6�*A�2+
ofWױ�J�-e��.� O;
32���[�b�$;y�!W�P���;�\^��4m}\8��{����`�HC� ���'=��.�,gε.$`���S*�0�k��r,r*~o��    E���4�0��)%\ʁ�⎙���;}^�3�̼.����ż�j9��4�)���K��t��ʤ_��1'�)R�>?6��%'����c�iu{[����5�xa�����T�%-�����(u�y�TM�Hq˽�fS�,t�,����6	�Gn�}ӵ�./o�h��'h28Ũ%�F��0����T���d0� �>˻�l����"��z���4E��������V����_(ȔkC�ΐ��R$=�M��Y�0H�RHv��4�'oؽ��o5c�Q�O�l� $�>�m��R,��d>1�P�Ӓ'4�~F��i�z��%E"d
���)����L��:��[❷�jw:�S�W�0�X����5�5%�h'J4Jf/m&�P��)/a��$���n�U��c�\)�L������L��p����o�i1��������>ޤ9����K�^�������<,W��Pj��Ȧ� ���/H�9�{�w:����i^�	����o��c9��y�:��]n��ͨ�=�e¼�) kO)�_��,{Y����Izv���L����?����d5a�r�J���!uҫ+�'m|*�{���h)���[�q;��Q�)�cGUm�p$O��=�����y��N�B!�'Xm5�L�(W�;�#%�bȿ�O���:eN.�c���Y�[�Xߚ�a{jxc��)����mk�*Qu(Wrה:�
ɗv��C�=��S�3+�� ܈���a������nM��]�.J����gHf�q�&	I��pRR�i�����WY+�0JDH��w�>�_�X��v�l&{m��m���H�o?�X>	m�QH���/��䁰�@�2s����7�_���΍H��[p�{��LV�K�8��V��~�����,��c��f�H���%����?�Ҙ?�7o��ͣ;9*�I�O��0��� ��Im�ݑ*�E���d�	��}�fNr$��8U�Y��1g������i��YP\���Cʹ�G��[R��6�lz�������3H��"�v���������(N1�Me��a��LC�ό('��NrR�)],�kd������sC�бu�a�wxx��k��5$4�&��
&��-�>����%|'��-8��m�
tHB��t�#�R���<��4˄Wo8���n?/7O.y�k+6�&���yL�\�T�7S�g�������P	Ĺv�)�8w��L�`�:��P=�p�C\@�����i�9�lQs֌�l:(Z^B���G���y.�כ��Q�.��c����������/�Ed��o�p�)����kI|�xy�+>�D%���N�@����C��<��?����̗߆�1bI�[�P��b~�Aw��r��AT��p}�����g@�2�ZЙ�Ym�"%������
x�Z�P�|��AL�Sq墑�=����f�y�a���	�����9�Ο()�*+W��x�3�'��l�I_}&�&9ݶl��V�t��hS�u������' �^2oZ��REdZ0O�N6K��@�/�:������]2ʐG��˿�![2�����}���"?���$ �"`�y�f�Ց;]\��N�V�`ʸ�̌D�k�9�X��|�U2<�)�>��+gabp{$��H橊&ߪ��Ǉ�@�H�Ud�Z[Ms�H���n������K�nM>���]�����<�|A�$�$�����߯DO4��j<L��y���+ޫ�"�p���]�|�19�?P{��d�;�
�(R��z+���s��\Yj�2����~�_�͒�~�L��*b����O���;�Li��Mq,���@�B,���aUă@�J'h���8Oe�?�1�:�a�Ţ��Cu�sn�c��G<��ٷ���C�$����P1�v=Q�2�|��d�P�˻�n]O�}�eg���^҉��\���$���9���=͛��a2QFL-7����ڄ����﹭Cws��w�b_US7��0O�h~���ýP>�Seo�����(_#mk���8�T�a�S��Gj.gfj��hr��=rt���F4VD$[������o�E����
��������xz��Z�7�@����`�	#�em��d^ur#w��W������oƓ͇D;y��㔤}(�J��wL]����:�a�6:G�p�׮<WF�}�Y=��RV���ײ��k5�|��������ؓ��(\�r�sEȸ�{S��b�r$J%U�D�?����G��L2�~�2I=�����TfĦ�s:�������[��깤y@���9���ڊ1��ɽ��bӹ4�|���$WY�Fhް����;mf΃���N�S&X<x1�ũu�S�|��V`7�3]�ϥ%Ѭ��YVr�A�m���`s\B�5y��}-�$��*��M�/��mi&��x������$���D��Ӆ��;u@'?�h��w��y���:�Kܩ������#�7z�3�&#�Tw։�|��Љ���Hݽ9��ye�ĿB��$Cz�g�<H���6̽]�EE{84�i�9}\��1r���,z+�aa�< �"��)s����B��S��A_���_�>�>ۮҫ����=�Ҝ�o�P��`�������i��w�����˴�c�[�֊���j�	����)�,�ߓ�[P��9!Mx�Ε��'?��P�B�\��*����<�1�H��0>���7_��b�0�>��R"�~-M|��}!��^�j2�����{�hi$���'���������H� �<6d��J�.��-�ӕ&��R[��C��aX��߶�������Z�m�?�R��ᦝ�s�9�,/tw7�⻷��V�́�#I��ˈ�j��L�0����9Rm�kRB��v�7���$fSr����C��b�jЗ}��6I(-��M����!L�B�}��)�R\K�ihX�X�U&�$��d@���u��4=(`�s`��vy��֓��Y��٧�mfo�	3ס���) ������x�_B2��SHs60r|ȱ�<��2i:�ia _��y�D��Y7��&�T�Z�8�5�t���SMy�Dh��T�����>$E➆�e�d����J�A�@�<I�a��"�B���Ȥђ\�a�F��j(�l|RC<���T��d�9x����q�
F쪁��D�'#�kXI���W�T��Q���?�Es+k��8��eo:b��S�[]C�mE"��6�sc��G
�)2��Dq�M`٢���'E��0�>�=0�j`�o����O�-�����G���̳)l�g�̳���4��8\�_���c��7�&�)��Q'�vV+��&�6�,D��O��R��Ò=���m���uo����%�<"�4�x��Q�N��r�:�(��&;k�1-2%�W�S^�K�T�+q46�]9�wZ���-ƴ���� 1�[�:Жv�.�|C�����;����M
��3�8��%e)�d���r���G��<ͪPі�d�K��	�qbټl1��Oj���ݐ?+Gy:'I���nϵ�*�[���@Z��q\��܍���9'0[�DNQ%���f���/�T�&����"��sB�)��i��
���1S�p?���IiT�k��v����x�yy�
�~� 9�wo�R><��Ȧ�mJ�))0�$���=� ��B��^%�
���H*���j��ԓv�cAg�i���2�ߨ���%�??|���`�lħ��Σ4 9]%oK4.�ۈ-����"�;���#X�<p^�B� o')]��zs��� �\w(��$��\ށ�L��;ՅƧf{��d��Sx};jj G���s�S�+2��?��t^$f��*뼩�R5��A`o��x}~J�a�Z�������(%���*��U`�l�P=�S�{�!��;���e/��_��c�A{GN���<��Z
�� 53c���IF�n��%���qy�yv�\F^�7�|B��D�T?�K��wÕ5��//!��	z�F=�#�\��?0�$��4�)#9��`��ZHB���.�<-��ʩ�Xt�K�ѕ�����|    ��t��Q��LyaIk��O�	��Dg
���'�S�xН��3�	��z'"N�v<��=�-S��t��"�Z}+Dۢp��Z�#G��P��ҭ��y����U�O�br��]�޼�5�_]�`���B��[LA�����C�J��#���_D�sh�=?5'� �y�<��e'x��zcDt�AS�U�Ԓb�x|zJ���ZK���ړ���G~{
��t(�6���qFw��>�y�)�/i�K���J���Ŧ=������pʟ�B���UJ��Bw�R|�}�υ�IJc~�.��5����GM
<�7׾%aO�Z�3o49Д��x��5CϧGƬ}_��`�iP�<0�ڣ"���-�h���W3M�6�?݂(���H��U�g�Xs%\�s�P?���kT�Q��6�'�H�Rs<��$���[%G��J��F�E�Rgηs�)�
R�PQe~���%����ϱ�7:H7'�cx�&�Xo�H�K�"��\И�m\��vh���
:�w�:�7��+��4?:����P�X5H���D2��	�� ��g$��T\��{~L}��H����[)��ñM�&ܩ]��&}]Tw��;�;`���BX�,�z��;����~��يl���璡S�H_�d02J�I��zz�GvVkV��it��hd�wn�ۭ'�^�ݪ~I�\k�Xzр-{��	#FKu˯���)���9!�O���!XU\6���Y��2���Ͱ�N�[Q�j}1�� @�I�ђo�T��\�&�� &K�V�w�-h8�ݒg��� -.�m����8�L8�O��P�ᘞ%I�s� ��Ύ�]��[�{���,�N��'|�97ݖ��%
�x1IK����c��.%%��$|�|�HLEj���q1OK��zq Z�s�J��bU�8W^B����\,�<[:.[�
b���Q%KS<����=��� ���y�(��T�7LT^�`6+�.s��EC����L����d�g`v Τ��bV��9o�ܟ�{�S�ԡ�����?^���T(��bJ1��Lܗ�Z���S!�gnG�+]Z~��5�U֠1�7�ms(J�H��l���=�T��ʹaŚ\mfV�;���ⴜOÞL�@���m�5��)��u
����a5e�vQt�Tϓ���;�rY^�6r0��w��>/����M�;e��D��Dj�fR�;}��n���L��1~���Ӭ5���xB_���sJ��~ag�U4�K��)��@�NF^pf�,�q�5�-��K�&��)*��g��K�BueM��u� ^�������>�Q�+cN��KR5ǟV���|�t,�Ο	�pBP(�BjU^=ӈp��<$%�:�,N�c���OJCSO?�j����]���2c5PH��������|��>qR[���}n\��{C�L:�)WƎr�١�<�0�+G�o�P���tP��gxIY�N�>�d�%���WN�!���̭�������4r�峒�����9n�8p�Y��9�<�M�ch�<�,�R�H@N�3�e�pV����dr��� �I�U(�s�[:�K��8"W߀Bg��;���Hb���q���v�Jc7�`��גt��^F�=Җ�殺78��i�9�F�a��z�Y�~���l�n֞��Cv}/���DSw΁+�������Ԇ����V3v�a'-��d�6�MK��be9�Ş��$g �~���S#�RM�]|㭅�yMߧ��f�'��FJ�y?Rz��C���~�a5�h3�\��|�ː��^_��s�X�a*��J�+y�(�
��L"i*I����r~����^���*�x^�� /�E�؋��cǚ��7X�Ny���?S"[����[���S"9?�@�/r�]�3����KI�2�M�����85-H��ʯ�X��������T�8ki���m�������9&C�H�@Df\���G������=p��q���_A��j4��l���Z����_�_G".I����ʹ-e<��f8�qOS�.� ���
3� ����`Uh�)�FI���QD�|�awG�S�&����#s�Hx'8>#[�A��m����UU����� ����*�M��4�/+���T����j>)�� ��%)��t���2�x�\C]yS4N�������Y4 ���8�>
��%j��e��I�ŹlN!y&2c�&�gz���\�M�Fe��#g��֚bXK(+��v���Z�+��[W��x�L�Kv��6ET���0R��gh^�炥p���v+-"#����m���fa��O�<W`نR�*��[V�{��H,���G���Պ��1X|�4$Z]��F�ϩ�M��S����-%�O�����Ϛ��Uh�&���9��:�s;{&ᆱ@��Mx���j^��ʖ,��J/�d�Ұ���J(Re����04%��Ձ��tɁ:Wꤛ;$���6�������ˋ=���O]�*���hW}<�k����Ts1��NI�?0���)!�4��py�jǗ�hȝ��ݛܕ�)� ?8w��׾7Z*������3]��B��K�����h/��.<�<5���=R����4���[N��T+�|��0�q�ӳ�&;�JnW*u����vO��>�C�r���*9�fp#�p�5���C�(mG
�t������*x�D�`~��is5�|{�)����N���,l�"3���wɅ����dU˓����{=�i�L����f��7���R��Lz1^yA+�\�<O�+sL}�"�Pӗ(򎠻!$���]�,�����Y~)4�C��O:cT�Y�1�|dȼ�q8�G_)&X�o<��"-Q9��*s��]��\i6Ns�Y'���ʧ��u$��7��E���N֥����b4�[z�F�t/D���Z}��v��_��O�:�a���S�I��Мpd���/���"_�pR�]����PJz��t�C^����b�<İ�7��_�UNj��i��2~G� *?!�57W1�y�*���RЮt+ N�))�Ġ�F`��q��{�)��E�EYR�3���."�>S�%����.)Ou��0�6ʦ奖#.]A�������J�n�ɨ�\2+y��i���ȋ���I'@%�=|e��0,��l���!�,�OGU:E�w�v�����_������Tιv�3�5ɔ�=O�_�]nIj�m9�F��Z����#�����u�h������Q������$���_�A�6�y�����ǉ}+��ďE]=�c�6-_rq��z1�,}ۗ��\���=�7�qA�n��mO�HאbĮ&%v�IGz�Qhs��R�М[:7��i>R���_V�� �\�ft'8��'9��*��7��D��"������!
rW�ohCɽ�HH>�^�����+�h��~xOI`���m K�g��N�my�Y=�"=�S�}KLy.�
x�^ ���]��Rϛ
����V/������_mb#ԡ��E�X��%�>��*,�6�|�\_����T� ߩ���P���M"���!�;�������QI��	�`F�P�=]v���a�h���U�)"|��B�Ү����J� BҺ[M�0��i����'��ĩ3I`*0ǙϺ�z8�sX��֓ S��N|y%�3�ʼ�F& 	����r�!����-zr��|�����R���?\��᪖�N���L���X���S��E\Bꅀ�r||�r&�����u1�����~���z��'X�`{��I�T��a%���}���<�3+���M&6k/�0���8/]-c!����q����{ �����18�=ڒG�%.��e󔉴�~z��ʑm$�qµ�˘>�:�!�}(��'��6%�$Լ4���S�O#��j��?�W�~6t>w�E>��Z �81���S�Px`2��H���3�F_�z74HO�"���,T=u����$l�*�҄�� 偼��є���*G�P���H�����m7{�ξ��O'���������m?L  �&Vy{:�D�����@yFh-%	m��ۆ0y�#�4�|"�k����t%��;�"we@���O��eր��n�B�������iz/�&�@3�    ����V�u;s?R���1��ni���P��֝v��ԃ�P!�bb�`�X;��D���S�>E��A�*�>)qz��j9�R~:��Is^���x�a�����(S(Q�V�V��|Ls�H�wX(5I�`��b��}X�5|�(���F��3H���>o��� �>��*�K��5�J�Yj�&�9!��?�����y�)4�)%��@W`�6m,�n�T�bK�I���]��L����Q�����˘ϧ=a�,ч��oˍ���I[�bE��gO�=^!����&�'
��6���	S�� ��?߿U[���>��)�s�������� �K?�$��?�ß��	ȧ,�ڜʃ����Y�qs���f �P?�!�t�r�g{n��V�|e�'W�������zM�!��f����W�Px.��5�9�	(������殱�:����Y��,�5�N���\��I3^9���PTh`��YGl*}��*K����"�5�D�3�W�S$:{����q"�,�9� o���%.���*��kk'�R����D����T�G���$q�o�,l���z�T0�z�{�>���p�HQ2�ʁ;��F�D]Wl�z��%�4e�3���Me�[��"W�K���#�S�V��t2�nmc�� �������O��d��u�����`̻���D��T͂���� Jv�2��0�>碏�Yh%z<K���G�I���-�Q�������,!���C�sg�j�, &S�(���\�kO��1���b��'^u=)�&��\&�û�<Mi��u����U���&^��y&����Ly��3O����I�NŴ��V�@8�G)fץ�DS���G2ß��.F�ЧJ��a�]pj�ŻZ�amdf�u��g)Lm�o0�����y_��7���0�|�O�-���rK�k����,��Z��`]�C$�?��{/��Ԕ<L���6�JVd;sE�L��6��h������9��\�|Ł7��= I���q��2=Aoӣ�U~!�4=gnj:O'�xc5�^�3M"�1�V"|)^���O�䭡���#?�j�tTV��5]��J��/�s�Ԏ*�^�w��:�O/<E_Å��`�t���8���WZT6��4�e,�����ׁ`65�E�:�R��g���Lt���3�1,��7U�)�N��j?�C�T���'��H�� ���-\T:s��W'��G��1w���ޒ<���*׈tPU����dg�g�z�E����En�ř��Y{ދ��>�0���K��j�y����9i�Ҹ��O�-�G.~>,�;�t]��Q_�s&b
�[ �8O��%��[��*d;7�eӓ��,m��bC��"Q�c��B`�B�[~�)F�͏U��?xrXڭĒq�Mj��Tܟ�'�i���v[�K�� ������T<�I��l�A
}���~C��n��[T�~�y����Z�DL،�I6�[�C���9m^�2sc�86ga�]� �;�E���y��H9n4x��e:a	o~�#���u��׌GG�~
5O�E�1��{��[���@���u��r�l�W"6���x�(��V��9�a�HRhm����L���ҾL��679����Ww�?��_hx^헑�ؙ��Q�A����)���9��`C��}�=hkTm7�A�Jw�(���3u�X)]��1!|Ah��+�"�8I�A�gb��4��V��[��k�+s�9�֕ZM#5+�����2����Ca=t��� �9��HN$b����:�b��↞�Yۧ�Ҕar̈́�n-��p�L�%�ӕ���4�IsNb.ͣ�L?����y�ŨK��TM�C�Kug�?٠|y�f+�Jm���|0� 旷�8�ǻ^��'���,1p(�Գ8�k�S3nM�S����$`���~N�J�<��D��7��@��4�0�O�$|�:=L�w� )e�/�$A��ִ��!(o�6���x�(�'6לGs��&�4 e*�P8q��b� ��(��Yug���I$I_�K� J���t��r��
��!�օ���0S��i�l���P�,����O����BB��'����?�N.�i	��m�����s5�V� �WzB��j��z����!�Q�};uִI�����V�?��t�~Y�?J�ŝ�¨�5�JoT_o0�RG��J7�e���jU������i��,����?[���i%�/L�~�}7��'u�ym��#��s�cUs��F
����ޟ��a��觪�x�����V�<z)M�.�m{a��}]���F�[�U�7�R(��,fR�MiMvJ�˔��z/φq��V��̓zK�6�g��8���Sjߴ���B=d��L�Y�bN�ҩ>�����<!� 9�L�s���"��!1�E^��X�0#m��l����*ᙏ��I{wK��0k������u���пOI@�t�����<Iu8�ل�6/g�F�#�܏��s�ك0��` ^C+|��A�^*5��
�Z�ɂa(��b�@�%%{sǼ~��1���/Jb�Fe�'&��])d)���@R��!|Z�vԄ0xW����l)�]���q6s�x�&��]�?;�*%���9��
��f+5����L�k����9���_���3&��H�e��r{��\@Cy���S
�LJ��Zl��>5�`�GU�E� ]b��j��a0ALո"�*�<�k�ӍѺ�����s*����b7���G\�����|l�t�KD��n%5~��:���7M��Ҥ�-���3��_L|e�b�!��MZ��r4�Y�L~��yl����>8�������`/U�~^$��p��Ҟ'�a��R�R��O�k�Xf��w�S��HY70�}K˨:�~OMN+��ËXR�����{a]v�1����[�^M�	�n������t��ũXq����l��$T�ӱ�R���r�T���1�:��K���u�b}�P��}*XY���~8i�u'ԟ��N���0zϑ��>�5ݎ�s���N`=�n	����Jn|rs��r%Il�+NQGvl�����g~1+xC���|����G�lC��/R�^�M�S�nv��A4�J2=���� ����,cc�L�^ă����]�\�T:9r�<>���hvW?�R E3��i^�=H4č�wp]y|JΓ�x{|��t⢆�����x=�MA��D�δ!C1v=_��똊R �mYPm���%��2��.PI`�*9��1�T3Y�[�|{"���]�v�X+﹐�j��F�>]�佢oͺ���#M����4SG>��Щ-���\�e���h����A�"wQ�o�V6Q9�~rͥ" 46�6��Ŝ#�[�9�+�9�����=`VD����6C���e�OLJ�xb���U���!<-��p3�Ęy%���-Ou||x��{����j�a|�+��x�j��R���H�i_ j8��a|�n�R�
��%L�������(H���o�#�?�$Ic���`�_�	�u������-#o��<��x���؍�(�>�
��3o�Eu�GT���}��w3�� �� c�n�-�Z�>	�ʱ%V,�o���Dv:�.�nA�i@�@Y$���i�:wJ<4�v�}̙�jI��%�W���m� F6��,��F@�k�rZU(n�����ti��f)��N�����䂥"|nٙ]���(�8�ܔ
;�A���&O'�$���e[�.�{��+3�r��a5r6��S�P�ã/�M�>*�s���4�/�<_携1��Ԗ��&�1���'�&E�Pj'L�_�Y�f$�� �%����M�c�0g ^��q�W����H�I��^j��A��ɷ�=�H%�bmN��q5G�Z�qo���]�yg���87�8;)�S(��;�%���_t?d�ĸ���^S��K��`o���U5`"3�A���0��t"Dz��@/�ڧu\�'o�|
z�RR�$;��a���9�	J����p�:�y�ذ_��	0���R]�ո=�%:�����n+�jH��F����`�Fkv���M����V�&$Å,    �4b�V��yWi�r����P�U���0 Kk��� ~�Zb}؊��3��䱈�y��!ɨ�V� ļ1:Q��87��1��R���L����!��`�ړ�j�&\�����D!}Ilږ^��d�/E5WNj nO8{��@��� JL�_ynX\}�t��ϱ�:�dR?δ�����S4q٩~_/k��[�B ��`x7oH�L$MJ��;��e�ʟ��t��f4��Rd`�����-P6���>�ԢW�y�'q�nH9��\)쬮㰻+�vj&f��t�)����q�&�,��~>��?���B�C��A�D��G6�yX�=�C����-6�cq��*x��!����0�=�34p�P'��ث94y��}[��㨥��ĿC������l%�� a�W�ck�e�8��t��YP�3��s9�;�j*\�I~Qqv�u�G~�{̫X�A���oQ�����H��oM����5��%�XI{����ŧ��K��xӃ%�d3ɬ&�dM��ʒ �c��t~"�mS��?5����b����/��<�bmZ�ƆZ��5� |X�*�n��X� �ٻ�����$|��N��w��K>-w5-����'��H��n��T��[ɩ��)�֖�W��H����&�59m���0X]A�M����a�I̻L:-�,e�WB�υc����3!�q3�kK�+�o��/�Zx��_r>���\�.�I�\۝��H��n�2��#��j�6����Z�����>\F����x*ۈ��)G,~L�Wa/~��%������<C���Ӻm�8.`���
��6**���<�9s8&���E���!��2��IKe�6�	J.��V;�gJۋa/_m܇D��g$ˮ¹�Y�LMg4�HzO� e���F��a?zJ����$d�TB��F����^�Aƀ#Ŧ���E�-�m*h�P�rKA�&�fW3#�ā�>�cq��MU̎q��ַ��}UW}�8q1>-��W$]�,�6/���.�4zE�FL�^ �sW&n�S�A�D�-�o��[�M.�Ʌn�u��[������?�>�"w?�@B��Ӎ���������P�wk����^��Mb#{��F��k�6w#DG�V�뙃�r�Xל��=?��+%ƚ��%��(�-m�;�xCV9JP�J���nM�K%Ŝ�u/S���N�H���r����^j����if�<�X+KU �����o���8:ȩ�<c�x��4��;r��k:�� �ϛ�H�H/�����4�.��@��`b3C�{k�+A�|�tG1�*�7�T[*i&�ϼ&���`��-�$ ')�7��lY.�EJ.`�$���t�IG$���:tă�B`V��\`�-q��gJ�w��J����D�?5W�N7� 3��x���K����mk&�{KRA�X�ua�k�@k3����^N:P���va���ҏ쑣�j�LI26������`��=���x{BN��;�Tz3��o��#�?�	�WR��%ݿ\�����Dڮ_�M^��2�dRɳ��H�4���Yg[��1�}2y�ǒzL@>"��$��@
�����cw[�8W�L����څA73ae0r�>��yRb������}y�&c�;�n.��Vi��3�rZ�{ϻ���o��i;۷���O�>ɚ@����M�H�|��������!Y��5����=�j��!⌄ɤg��ڮ�mG�'gw�s�.C����i��״t����j�T!�`'n�|�p�'�GFCI��u�}�<�m){�m�>/�X�g����{[��'����D��Үej��d7�bq�Ve ���;�Rr�J�r�?��D��T��H�s�a����$��0#���߮�'�s������cĘ`9�:lLDZ{�.�-�)-憒4&o\��^��e%������5�I�	g��gƳ�?�+��$5?�yM�Q����cO�b�"q�`U��\��]��y�P�d�6;;QM����\��[t.-�>q��J�������!���b,^�n�8�?Ä��Z!���Ǩ��D��hTo���F��D��颥����]K��Ns/RQ��>��_ww�T9Vm�T�hFd�F��y�C�)}[�6��ә[��`A{�Kuq��R��R�%!-;�W�t�T��N�"���)x�3��S١܁Q�'ٛ&S����Qg�ӂ��{
��ׁN�@���H�Ǥ�y��a�Pai���N��h�.�&�(��	&2b�1���f��` ����%0lm�O�e���[u���t�N��,������l��Q}z��ՓPh���ļ&v$�0�)C�t��c�'Jw;}��Rb�=���$�����`�p�,�����T\�w�a������݌�0G�^��ڞ$�BPҜO�|�U�`��'��π��X��?*G�W�T[[T{�j�iOI���r�K�A�nLY���i�B<k>/�|k<zU�9݄�>R���?���8yU/vs4:)ۛ���(���.��_P��XP&2)�Sd�U[~�A����n"����G�q M����;���~X�?��i�qk����p�!���
�E=�g�y�����@S#�?SOh�1SfN՟�o�t$�%��F% e*��Yi,�!/�XN璎�嬎�2Q����vᗐ$9�3M�+OU�l[v�$�1�"	��9)�H�%���x��6��ۈ5��u�m)�QN�FE7�:�"�ڛ�J g_�mg�=t̜u�?�|��՞UD)���0�zR}��%L�I9^>	����(?�҅;� V�ibn�9�.r��DZN���Ϸ�ʨ;�9��'��u�������{�c>�� ��v��SH��\҈DO(��n|���cH�V=�?�<���[���<y��Q�����'�,.J�	v�9'¤Ɨ�gn��$-�n����\�.r_��w��֛=V:����c:�D�O�0V5mת_4\'q"bR I��֒�	�$���G�����(
;��ΏbHQB�3����k���J��%�u��a��\�e�]%����22�<(+Öy��r��yd{7�7GB���=q�?���߁Vk*UY�i���/|���$�#e�Nr}!4���x��k����$9�|��X�z�iL;�f��M�k�y�<\�<���9��8/
a����g��+w�>�┢��Vh��J�4?��،)��&kM:/k�&c��&g%�l�	v�_K+>"n)ny��U���ib�'��`e���?b�J+��yc�+�}��jg��ʋ=č�JC-��U�Q|��"��9��,y]	���
rEz����l�ݘb�������*A{�^�����q�I�;�4E�|��I��CL�6�]�>M&��S�S�
��a�LK;��Mj���K��Uci��B$�3;w՚�dw��Q�Lk���u����͎������Z$�P�#(vpw�tqrh�m"[��S�Dۉ��t�k+�X���G�5��)B�fĀf) μ`s�ɸ#M�Q�7A�Ǽ��e��C�`���}�#����_d5g1Y�=ȐG�M�i;�b�d#�\e�f�:MH�����dSC��f8�U(����+��I��H��2Z���'=&%�,�;J^�M�U�Gi7'�vg'�`G����$�t�o@��~����'��_�(i��+�z��2N�k�s�����,K�m��"m�I�Wg�?���n��75��������g^��S7����͹=I9�~�j�'��)���_����ozR����O�> 1=+����}˿tb�',�(�}�'k����h�_g��jG�\�̱��	��m�Y� �7D���oh�fcd���q_���P}�H�c�7@Ӧ��xӥ޹�<���6�ـ�X�(x�ʥͦB�bj!ܕ�e��&c`��Sf�Qס��#?��R6=���{`B|H"�?���X�]'���"��;�v�Twg��΁-Y���L�O�?�p������Ni�7�f�O�	o��\r��B%-M��*���}��3�K��F���бi���Gh��x\[>��⥓��0$;����0�%N�/]�F�G+�&Yz3����:���)e S;�H��W#�'g�6�N�v���S��    �gaw?�mS�+_�q>���3)���v	32��{JN��#�=�of��lm��U�i}�k4�乭V`�
m�XV��T�)��u�o	�)��4!��,,g���E	�)�.�tр���a��Wjq�p��y�G`�,[�qΧa�u��Jp�^�������1�_��hl9�R�n2zN�KM�;�o���a��2���;��w;�u��}��p qxf�>�FǓ�p�L�	�.e����.P֛��TJ���Tڋ�N
W��XE]���nr�{�?��	��8��)�`�����Ͼ��dV}I��"����mO�:-}�x~`$5^�,�!��9����Ȅ�e)������Uk���^����oAX!"y*=��9�/jk��Y8� N	��H�'���0SYÖ��񯴾�{�Pj�}e��k�U���ጠxR���!��&d�|ޯ�/f䌷��;_sXg�s�T{��G	�	r�y5~w��P|(�h��C����uق����6���4�Y<H���Bw=]�^��4�֤��[%��E��H�����7��6�j��<�<�X�ዛ���;ݵ��s��i�嗱��?%yQTJS�En���M��vb��֣�N��{�z��-<O ��Rp��3I�D��sfr'I��Na}}0����]M��Z�

�Ҋ�T����}J$���ZHZ��VFǼ庿���x�T��N���ʍ:a�xU���rԆ�w��NP+}����H���\��q�BM=��>�B]�}�	� �P ��͖���i���(*��Ö����i��I�{n�N%��o31g�<�m�5�>h�-w�����yE�*j/ z� �&�DB��)�R5\R�X� ��}��� �����/���jK٫DD8��s��;3���Zp�DI0h�,E�zp�3�����^��ei01�|�({ن�?X����/�&��]j�8�}�ƒHѥ�e7�jK �%s��z�x�t�~L�!��Z�y�q�h
��KE��}��c��2�c�0N�8=&�����������}��zt�i�
�W����U}������yS��!�}�1X�UK�i$�Vv�f`t��{a���n����L��N�g��<Sc�7�˥04�)|��r��-�V��^ W���>0����r�2��<�]�q�e Ͽ��2���w��^���ˬ9��V������u�7I��_�F��|�V오�(�MrrkK��Hޛ�Ԅ��^�Px��4�쳘]�����V=7��)E"���tQ�I�]���!��R�8$G�OG���	�\
1�@"_/��i��)P�"4�������8� �P�Հ4�PX�<��n�M4�S��)��t6w-k�D��S�<n�y��	��_FN|�ȯ�;�|b�5��l��Տ:s$�̔7H��%��~������r��� ��͓_�z�r�����ϥ�7����؜�]cOݛ�ܛ]��_s��G���s�<�X�^��A��mD�#ـa�wɴ�l��qo#a�Oc"��F� ������^ K���[��ZJօl��g�}��a��+��o��"������7�!r랯�]����+��,����3�����T &7�֗��|�AS!&1|�&�+�`��+�z�n��H���k��w����(��P&�F�����Kn1�٥���eS�BhMu�Ӭ�5ͼ�f�q릯f�e��� ɟ9x�7��蚋H����xi*����:-�t��އy�a�z/��-���a���-��i`>4�rV]9�,�e�^�%����v��9����m�o0R�|Bpr���B��%�xj��-��Q2JS��F���ݲeW�Nw:	d�神�3�0��HG��z�T��䣓 c+��3��	�DꗁF�\������W���]�3j΄,C��W�@Z��q�	��(���8ikz�ꊋ<n�ȝWtJ1�H���J��-�f�o�l���2�5E�)��oY�S��8�8���Mj�h��l[�5��4�̡.���e`e���.^�"�''_��J���/4+O�Y�I)�@ϝ��M��8� B�{�?s�ҾO��d̀�i���b���;E4�q����45�}ǒ��\�"�,�ү�g�$P�(��4�Z�n�����-�:�<�dY�9��u��W)w>M��*`���!�xx�	B�����20 ���(_^�nG�E򏴘���s��whg{bʏ�N�xʡ��*�����D�F@��TZ�S��J�[��I��=M�z�0��\~���C=⏫RR�M�p���Y|��Dr+������0�ZRG����(�y_���٠���
��޹]�%2�0[�<������ӴH9�$?-4TNZ�V�d�}~��n|��3f��N��K��&j|��2o��7Ƕ�,�{�p'^���9X�9�eK�-���N�:���V���^[�?��e�~e�5�d��<}�3��o���h�$��<�"V���.%�ׯTX�4�-V>��]��(�� ��{���ɸE������~XA��圂料���F�j�|�|J��E��}?���,[���۸�&�~�;S���o�66���]7���s�c:��v��.(g�F��	���񴘀�l�e��Y*R�JP�h7�|�oR��=��)g�!��`�GEq���nl�@]:�Y�n'O�jx��c����K��C �����䑃A�|�U��k#>s45�H+��=�!�A�`�m=�\w6^D%��9�ÄM&k*��6��»)������`���XjǑ"�U�_*�]e������`s-ΟG�폞�������0�ӗ���[S������R�"7wK�I,�^��n��e�<뇲2����t}K�n0���Q�Bv�����Rz8K+5Y�xf�I�E�>���J:�R�#�wV��,���Y|-!���
Hw(�'�L*�G�'Ú����vd9|̨s�G^H�m��4�*�%7�d�-�Ǟ���	O[���I6JHѺb�?x�d�p�p:V���"V��ȡ�=ۿ��\𤴤��"*��q�s�oۻi�,��%y���&b��{5W�����ܰ_��y&6~$9�`��o�iJ&O�İI*�|�2�6ʝ$�6�>�����9o����ȩx�
��6���v�ګa��[���1��Z���r��Y����u��v��V.j�JTsO<�S�I��������)��$�k���)q9�=Ƌ�#�[��x��w�i�n��Yqm㔏�[i�o8�|���̝q���:�H��#��L4�W:��4���_�~�[�@"Ō�sKȔ�y�[�����Vα��a�u92�L�� ��rJ��)�&���c��7�oo��72o�a-�n������&G��$�ok�r�18�2�yS4RA���y۴���L4m�D�Ĳ���*̈́�I�	vnq��7���������j���>*��\��/�I }���0��xxG��ɭ�����D��J+�\�Xp�Ͼ"���m=
�QPIڂ�*�x�tRBN!y�Q$}��{���?iJc7w.	�)����`Mv�A��OU.�
���h�ĕ��;�`��}��,��ˏ~�ɨ?��5L��a��k����8J��n�[��C�ߌ�6l��I���t͟���g
��v�\L�^��Af'�׈�G4 �Rү�#O�$�p�Ռhc.$;'��2�d/N���6b�kI��x��Y�В&��N'Z�p�^Q�ށ�ss�BM���0p�|��z�P�^�0�^�%C����!d�,y� <	Ҽ��/��T���������0f2�@�8Zּ���d�^�y��z+��}�!Jc�� '���
���F=� }�lK��I���B~���[�9�n0�G�14]=����%԰��L������F��^�}��Av��M�B�]l!L�󁖋<�U�H �w����X�%��؀W��������U�g5���+�T�߫�+��	��O=W#��Vu�g�xynƹ�v:��K���פy�R�9�r+}�#�bۙ�btQ���t���`%��z��˩-ft��4m\N�mn    ��G¦�?b�i�R�s\I�5?(xIBC��y!ؔWЭE�
B��C��!����B �h�ǁ��ē˶X����B��PE@fMJ�ۨw9R35���`�)9`a�s���_Q���{��'׬SU��>3��l�!�����{s�CM����c�W���lo�w�7CA�`b[��/�.���t����x&TU
�Fڶg��7L<^��~.C%;���F����N�$6�t�	��S �R�Lq܏y�9\��iϋ9 �����I���
��"o>�vM(�NJ,-j�cOs9�ly���p�sc� )��g��.����f�p/�% }��H�Vg���Ҙ�l�����bz�i��5[�����<���0���9�2�`�}�����
�^�o�
�`b�اY!�Jq�����k ,��^s�UBi��%�H8����=��@C��.t�����Ie��˽�r(��0NEpql�Sn�]'�L�`����c�:��ҿ����ec�<�o�����-%�k�OA�'�z�"���Y��~�)7r��������aa?���t��.n���\;�_��gy>����{����6�kI�������h.	o͠���6G�?�FX<�:�x�I-f�kr˯�ܔ��x�v��G��� ��R�`���ȋ1b.�=�]4�P�?]ϛ{��)��5�c��9��=�DxZGJ�4���e�?	����	�7r��I�r&Ye��x���!L� v傳<�1�y{1X3��6<�� ���oW!��_��[�F�m2;;8���dڪb�ѵ,C����8�ܷ��$�ף�ڜW0J���R����/����+�:��r,����j �y�X����(7m���S#���F�g��7��f-�}:���k�?g.��d�DCS�TG,���Nݪ���g�t��#޷����K{p�V�D����V������k�`���I�ar�ƦKY�X0�_}w6��h���7�K&n�G?��o3]`�S3=�%��	4�~^H$&	����s���~'��b�m��gP1�ǊZ�.��~>o����O��SN��}:��y������[L��OI����x�<�+�,%tʐ�6���NG��3�Ӆ"wOQ�G9��r7��/����N�L}����ҁ�,�x�1�L�e��8��{I�-'��u��p�i����4q�'נ�O!�>����?��!�V&�&�|͋y�<����a���	5o0q���/+q~��kQH���S?4R}]|�W]�A$��;J@��/���\��fC`���Q��|4�l��J��t�z���>�08����t�����iL��E=Yq��9�����a8_�r3}�����]�bˈ�t!.���>����2�)�f¥@[J\����'1��܌�?>������2�_4�MW �'��o�o�O��8�7��C�<̳T��k�Pi#����*X�]O#�41��j(7�Y	�<����(5��i�.D�d��$�n��#���|�Bu,ʛ>\S�jC�|��i��3d����z��|�|��w�ޢ�8j��`ɶ'��c�;؟��m�o�=��:p��+L��"�Ih�ï5q�YQa.S-7����G��]�>'-���;��L�����)4kŞ��wG�`�\�5��&�D�"a�~���Q��{���WB�f���2�>LK�?�}λ�����Z��x0�a��	2+Q1l���$��[�<�.3n[���FI��W�8$GM5U�0C�F>�����6�*�tPHSk�r�_�ʉ�\ȓ����K��d����7��}\?����,}0��j��q�y�U^�;�)�����M����˼<W� <�b.����$v�w�Ӵt��}���hF��&Krե�B����(L��v>�tBm��;��������@b��l��'������43f��iG�a|�R���^�'�g)�҂��I8�R?}�9j+]��Ip���Я�ٲ-M.�T޵DJg��۞c�<���)�(1d���A�k�/R\�s5J�_3��?�4����?k�4>F��������Ǩcb������XHt�7Q�d�?��9��R����8If;�
�Rs����%˚���IW��~�ȏ�̤�^3Je�l�R(k�P��ȇ�W�X�`���b��ŵ��6��y-�6���a=pG[�r��QVp�Ӵ<��ŵ?rA��4~r��#��%_�R[{~��)�.�6�m�DP�j@��L�&�Dc���>i�a5�L�}m<����*�Flt�ذ�E;�,Q�B�L�^�9�cc9%���������m���<rґ���<�+�Q�\�A��>��6���7(͎��r�>M�dX��Aq%��}
0;��<�k���ĜȜ�~�ŕ��<�a�F~z��teBv��¥�/�n�F���WNu��~^��L�*�*�ݶW�3��^���=6�vt�Y��=Q�W�nA��o[�A>p�V�bD�#����R�%�'*ʧ,��p0ݪgNQ�fG�(��ѓ��N�p����]^¾AB�	��D�>W��f�M���`�����<PSg.���H*���%���V�<�͂��c#���%�Ɛ����e��S�?�Gt��W� qgY��Z �:����}���S@T�D��˺�K� ��G��֚d8"�2@��>�XW$*.�t�~���N���}���S���?�����q�J9~۽�\� yEy>i�j�w璮vƗ���6�a�O�c��>9��Uz�C>�B'�o��\� K2O V�ON�}~����d8W��j#�4p>�,�@^�8Ҵ2�N@[���]!�Z����5�y��t��%,���+�=H�O�q���>���0���AŜ	�n���}�>f�@������� <�\}3J�rF��&3�әz?u!^�1�\
��~�u�{�x���꓋�0���*]."��l����0�̢�a}��h���0�9lQJC�XQB�Ƥ�����|a����S��/���5��ͫPa���>�C/w��I��"f��E��g��9���$��t�E-�[Q*O�&��V(2�f	ϣ�;�^ �t��:�M��s�����Q����w��+��;l7v^���E�~�4�N6��Q^	���ݍ� �r��Hh4%�N�D��ɐ��-,?�����Lix�\�Y���f����PX6���3(?K�؇5��
I��.j$i�-�(i��!�v^A�A��z."3��rn�����wG:42���[�jHM3/H�Ǆz���v��G�#+�y_R��8�����W�3DΑ�����W�>��)Mh�$������|C�t �]P͆[I$HF�q��TLsB��Ox�E��|�A��X;7����+�M҅��*������Cv&r��QzVԵD�\�-f������R��;��Z���y�3%`�;G������N�4PG�(�
��oJTA2��
XS�Gs��a��o��X���_J��������7��w�tE�hy��rI(��1���dȞ���a��,:��S��DO!r��nxy�7�8�{<i>R[�q�Z|�I�q�7s:�d���=� 1���L��7�7Ҕ�ɇ;�@�y�Ͽ��>�џ��t��֗���r�t�*R�o�?R��OC��mOy9rq�\2�OY��6��〄2�榰f��f̅\Q�����rkJ���T'>/c�%ɑ��O�6�8�J�T�y��&dߒ RJO����L7�'�OPN�[�R ʕ%G?��	��/*Z҂s���ݬ�Ý�35f���X�`3��w��\�Q����d���ї�/��(���6rM@?G6���|��1�����6W0E�VU:dk�cb��g�?��G*�!��$۬���✴��N:3g��2ΰU��'���2��kנe�c��R*[+����T^iG�Y�O����`���r��WJ)|�s�J�D`eq�3�<��_y��(���->	L�K咤���ͯΣ+�'_g܀-��P��>"]�������\��_�wo���rPX�K�2�},?�xa��]�B"(�1�o��Df_�u    ��<��,s�d��fB��q?��@U�w/�a�34+H�m����C���l��[�X)*��t'�.Ӯ\H H�Ԓ ����WS���2�<�.���*-�K(������� ��j���h��?�!�������� `���T�Ix+�i�枊�n�7�]��ȭK�FA��b-0$]�x��YJ��-��M'ǘ�l�
���فRt�%.�o��#�ujؕOB�gԲ�O�y��mUF�w� �|���uE�I������h[�o�.0���(�2�G�1�	��@�Ӿ�6���(���Fl�TV��G�+�����:YltJm&H�>%�\�ҹ�'Z2N�u}~�ƀC�����cI!�_oSВ��'�l��}�ܞ�Up=rR��'Q���`c�\/�p��=o��P?��ҕ����?f4+
�O�`n���z��Q[��C�냏Nҽ?3�q�p�tckx��[C��#k�@)���c���i�"R�`԰�&i�;��~WNو"R|�1{e�������II!�o-�|�<��:Y1��t�R���X���u8Q�_�q���fME�"��N�	c�RҔbBn�V�[Ľ�=G��-|�-�[���YB��b���O�$��E:6n�'[�c#���9	~�=B���~L�J�M���4*��C��໙&i)qr����HƝt����Ƞ����3�lw4R	6����6_�&�Fo��&���h�3e?>6������v�کLk�k���`B�ŪW�m��p�W1�K^:&UP9Q���;cV�����KĴ���1���mg}o?!�^�r`����W7k�}�iH_��T�����p�aS_���L��~Z��������0; �����S�(�q d�2\2H��ʴ�/��4~ u�����ӛ�W�Coߺ��!�R��M2�aBP�$ɬ��W�i9�d]�zt 8��1'ޝ��u�%m�r�=�C�;�.�܄D�|��/
\�C�w���-�m瘆�8���{4����R�m�q�(�O����}_.\y��Pȡ��8��U|Z�5���)ϗ�ĝ[�$�����3���h7;QLE�'r�z	WM��,�Oki1�>d@ ���朋%}��E�s z$�1�$Ծs=��閄��^b���d��d</�Z���Au�%�̾��A�G�Q�'��w/�Z���s��Q�a�E���'Ե���,�q�x������?l�8��E(]���s[�eg4��ܥ|�F�����Y��jӂj������V�'un�PITdN��Ghb}h4�^�TY����)�,/hv������$�݂!�؜ľ����������v�IS[�~��'���6XJu@���H�xѐ���{z�I�OL�ɟ�?�d�r':	lb��(�F�6O%���x~�T��0�|��p�v��m䄸d��zb�F�q=�>��J��i��/�hb�D��o�ny�	�S�N�LB����]��|阌��I*\wҩq8�X���\��熶t0Қ��s����'��_H,v]J�D�#/�e�|�P���@OV��6��I���'�$m�}�'����gSg���H㚈�Adl�ѩ�ˀ@
}.Ж`�@���d�&l��S�1� �4�Z��@#�b���5��:1���u�����ђ�QV�?M�$�B���2��u ����?Q	(z7�AW+e�+��7���y&��0b���G�Jhz�j<���&���r�b��¤s|@��&�:������\s�$6�[�ܜy��;��-��Z����̤���Ri�ۤ���[4bO��,On/�p� $����� �,f:TۭdI��6�����e,�&�Q�VЮ�b���u�*[3�J�N.K�y���cvm@�qb�g�&H��oC�#���k�eE�\n�^�[h�@B�}��u��7RSR���R�X�W��������8����!˽�EC
J>S*�]����2K].ז�d�"��q�n�Ӝ[������T�c��c��[����[�y��\���y��Z�b�ό��Ͳ�K���)���b����0rQF��m����H�,�h�L��3.�9����0�w�6��߳��!��-�%p�6��%��\s�)���zc���P�qDIWtli��[�\,T��f븣g��R��[Vf޽ƬT�s�@���sZ9��I�_;�ѝ�����s#w��;��b��W3�?���1Lj�_FW�\^"���3����h$�{������vr�D���wڳ�j����	P�H>q�^��0)`r����y?����+O�&�9U�����Mƣ�p�N��tz�Q��Q���1���/�y�{�ߙ?�џ~��B@��~�k���8~ڂ�X�5#����N�������\�/G*��l$�蜞���G�8��<Xs�)/��B!�_�^�_J�m3y���f���Ѩ�UU��"UNx2~R�d���}/���Ԫ``�A�
*�|Ɵ��!
�\˳KH\)4��`�FE��i�Y��J7.ׅA��������-�2Q�;�yV�3E�<RtI�S���s>�8��1��f�3�c����u
4�Y6��v�Il�Ym��R�N��EJ�>�v��������K������Z�H�ǻ�yJA�,�_�`KC��Y?��)ÖVd�0��ʱ%l�[e������3'���.z1���0$��,�Ւp}��g��z��rC�K��D�D����1ٔGM��a�RE�J݆H��iȺA�QPo3�Zz��6s]�ű�M���'����2nZ)��x��n��ᕼpI+�ds���q�~�Q�iy _\�]y����Kwur��r�]�a Y̿��[I'�{
8����l�ə�6���>�8vӛ��|4\�+�U�ډM�k��Mt(.@/=~ɻ!?�~_��t`���Et��u�( �z'��\
�Ra��&=?5�<�Ł=����G@>A��ۙ���U�v�o���J��6jI��iT��R�2��a�?j�������{�	鉧�%�\ז�j��\}rir�Ω�3m��M���+�KR0B!���M�(�1��~0�K�-�M��bU:�	�.�^�m�B2 ?>��8.+�A~�\��/=�a�ܘKN������aJS�$��$�Ÿ�=�ڞ̀R��	�'I�;�R�(*�U�Z⺽I�u_b�T�7FͅN_/@A$M���%
���YE������ʳ5U��,��y7S3���j
�$����0�f{�[Z���J;��i��5�<���x/u����,z����ޚ��e��t6r���/󰕹��|��8�Ci��R��෷��N����O�ڿ�,ˠ=��F��.$#���^/��0Qݸڈ٤Ov��y�-3��ùc�h�M�RQL�����!�W0� J*&j�"wN�R"ORݭ/���~i$|�{מ���ѧ���{�S���_��}f�V�{�3������-��\x`���k��6�&���m�����&R��>V'߂�`�y����/	�f�9�w����ab����=	s��	��~���/�R�~M��9����t/��~X���Q!գ��Q����7�<���W�b��s##��3��T$�)��6���X'_T(Sv�'Ӑ��䆧L�;���r/�ś�h+2g�Q����A�&���n,����q$���q)��̦�: .b	�����/�ȼ/|�K�e�.;�䦩�Һ�T�������c�`0�j�3�a���UYu�N��{�S���j���`�n�s����������c���\f�r����5(K�@
;���vM���շ��E��Qh��SDh�]b�I�9��o�ZD#�)�6����7�Ĺ�5���M��gA�t�W��(��$~���Q�K)����8�H�?o�ïS��T%��ka�@����Rb\T�mW�p-U�RR�A�?�l$���/oQ�!U�ʛ:(�&��e9\�~ �����B��v lnŅ��B�iW2LΫzOS���3�ZN���Ʊ��B4����N<*l&-��Y6��'´�A��W|�${��qN    �B�� �=�2O��Y�c����ɷ|����s�W6%�AF�R{�|�+�pT���V�7[Nm�	ܭ��+�\����T��o }{�s��3�6fMD�6�j����`TlR�y;yD�Ϧ�>���'2m�g/r/�4���� \;��2 �}4���{�4r�4���48l_S`��~\�QC �27�ۓ�d�^�� ��Hw/X���JX�1���a(�kK�%r�`�ZE��y]y�pʧ�|��9pmp��զ�,��ϔ���f5[�̃&;{��+� ݷ��\u�{��y(j�1� �)�S�����𚈼@L�ou�;M��:���0��,%��U��̓��К��s�����-���T�T�ܭi�В:� �.`�9�q8�B��7�N��FYR����0@��%i���Ӡ�yWwl���Fzf-�!t�y������ϧ�� �̲r���aAIx�*,��+�����,��`� f,�����!�VZ!i��r�Pu4s���S=[���>�(��j��#�U��k
���G7�?���n���ߞ�#���G^�%5|�೚��,?��tր5DwS��y�
2B�L�:�D��9��6J���H4z�'�>-YT~\Ӓd�̖z��7�i�{���1W�%��;�Զ΅�>��IJ��c������6���gN���gʋ�gUhI�X�:Z׽��p�E���c��$�[>�\��H��p_Rh:v��\B��D�7Xʞ>aޛԀ��&B��H�8KS?y�ٕ��͏N���*���7bc	��w+����H��A,Y
�s).�mtD�;�<�UyF��cO��L lWj�-ғ�g�65\�4s��
y֤�|�)-w��&Eq��_�9�'��z@n�5��v������rѩ3+^�'E�ڙ��a�����3��zK��;�d���h����zͧJ��/�'z��<'�I�
�2��!�ěJs��h��a��F,�C���NV�]�Mz�K>�=HQ�Wd1�ʩ���#�t���j�?{�ʷ�
+��Ҫ�J��p7'��B��Ig4�c�ħ���D`�%��_1t+�036༳��!`�?Kiɿ`
��NP>v	��\������@f��F6��a�F�@	�
T�|"�E��Kp ����:ҏ�~��'4�������[��t����
�ȍ�}�?���0�����|e Az��GГ���S�� k&����tp@Sz�&��jR����a@�,F���'H����%�b85��.yR������v����]�3OC����N�+I���N����;���R��|��诤�h>cT���`�zJ�i�)=��� �.b����'�8��BE��T�;9��0�	�� !¯m'S�5�SK*�)����(FlI�A��iY4�<}ӷ��]�'uI�P��x�Gh.����:��eS7%�-%��Q<���:H�i���9�^���bmE�.����}u�ˌ[Dn�Q>=uy�m���5��4�/��,�8f��1����
��ZND�Vo'�mI����#6������ �9riښ����j��}�>)��^W0���XnH
�����x���%����y�g��ܟ)��>0��9�uj��JW?o%q���@Kcw|��y�+�[�^�Pk�� 4@�5�#Q��b��5s�����P�������: �|^��T6Ei&E�j��iۍ�ת��M����I9�4�	l��T�Uh�ǧ��|�V~MtP��-?0o��2;6��E;�'_��l3��|z)O/��M(R!�Q�a�g�����2-�qi��W�J���@��	���!ۧ��T9�UֽҚ���Д<�Fu5���HW\���G�����U}V�@ �r{a~NNF��)�8�m��h�Q�:V�9���ە3��)$D�O����(��eW��V|6E�J�2�f�K�G�xI�R)��B};4ɢ�wm��OS:�a����������A@R��+�A\�r�(ϟ�	KMH�4��{�\����͏L.*!	5����-]6����"����'U�9}4��^���F���_͇}0e��[�?v����L*'d:���Ӭ19�xuv�m�����铕U�U����F��tȫiL'E6$<�];�i\*LU�n�����j�bCP�H�c�K:P�XzyH�v��3�k�ܟT�qd�wN��ؒ�*��/������q`���Kn*����B���:�(,�8�o�6��~6��-w)�N�Io�@w>8���.�Y�0���7
5?��C���NJ����r�s���O���(o����fȘ�k)�jƄ��Ŭ���tu;��HH-�l⠘ݍ߆�i���׺�	��F��_9%�%��y89.��}�N�URg��ߪ�s|��Q|�\ri�^�Gxw��	Tֶ$-��N�<�p�S,����K>EΧǝ��-G��mǰg�
�I����?V2���ӫ�Uj�u�FA�υJd�JDen3�.���9�Ӊ�<�F+�	�H �Bu'�>����6�L2��y����-�g�9�`҅RDÂ]�WWE���N�H:�	l'�1�,�)�C�6��<u��nd��fj���v�	�f����L���}Є	Y���m�y?���9Wa���'�`�5��ԍ� 
���မI�j|X�v�sN���V��Z'd��}).���S쥆��X<MXm��SY܀v�U�s`$sQ%Ez�dv*Mg��@Ve6n}�򠺝�����Cp�/s4�$���P齏��MW���qܭ�ITg�Hځ��~�ٔ��r��AujߖR��8��o�'�ɦ9�$�1>����6�L��C�Pm��7��-��`i��O�B�X�p�|�Zn6���f�?�t11�)!�D����Y�k`�]6.���狟��	
8�h�+co��s���N$�Ru��o�B��	��Yb�uK��9��?k��s\�-��5���l>�E�M����pWʈ{�����tL�) ��[��h�/ �DN��%8ؙ1��k�&Z*�	��ci哧0:?dp����G�K+�g3��e��y���E��]-��Ml��g���?�-꽕��{�� 2���Hxk?�gb�Jd������,��e�b%����f_�2fپr���Თ���4,��R��sR̝�0�_~B7�͔�g��t���%��߃4���{^E+�޵ Th��_�d�}��sN��}����4�T�Ӓ���Q`f��
�	t��<�����# �sJ�7��(OCp�f��N7���+c:�,Y��t&III����(m�V�`ȻvX���lg=��e��{���U91�C���>��EJL�H��O���ߓ�݈��c�ϯV`��m�JG*~.:��D�C�2�D�m��vf&Pl}�t�9�9d9��G�$�����4�I�ݓ�T�m7D�9������6S��Fc)%!n���\������/���m;�Q�x:"��&C��X؊T�-A"�q�h%��䒤>H��:a��f`xM\��/m��p�y���vO9�y�/�0V3bu�ٛq���U� kB�mwu��J��W�c�q�1K�
͍��&*�ݿ�x5���HW2%^�&����У@	ϭO�#��51-�s
���So?Ct�D���N�ᛮg�q7�]��>��*)M��%k��Y�&r�oKk<Љ�ڤ�Y������{
Yi���H᷹}��鍿��ʜ瓗f7��_۰n�)!���� �T�{yb3��c')��=����Z��x���9�K��`z��\H����Xm�DUa�y��'N�9��I��V�@�F}	�u>�m=����dEp'�2OO �2i(�(���س|SNzD��R��29��B�O]��~7�]Yq$@Aqa��/���(�p�ˁ}�s�EJ(���Ĳu�>�<iBi���R	#$q��[���)�`�����=�:+;[
�y���"l��	��[*���[�~�9�F���13�ER�3�'��$���Rʀw���gIϠ�I.����BR�'�4LDS�?͖%OL��0��wը��,����eo"N    c��)a �&(��t�#4I�P���nSaK����Ж�ۜA��T�뼖Aׄ�1��]3�"��d@S�z��đS�8���]ɵ/���_���)�	�P\���_>zl���x��F��ҰY��,��`q�c=����K\���Ȗv;�Z0y�T-_>ڻ�g������A��i�.n60̋��TV8*�Y����\�f�dZ������ ���ɋy���m'/�	�fb�ԁYW$X>��q_��_G:�yэ�5�x�~>��p��Iƛ����8?��ES���9b��)j6��Q�����d �����9C�*e��5����U?i0]�7��Nϓ;|�R�ű�칷d�\�����F��=J
�9��&e[떼����e���Ҵs�L�h�����W<28�/��)�P�,x�����L�����r�W����Qa�L���)hδNy ���av�ύ��O�сÝ�
��N�;�C����k7u�b�>��-R�TȠms����\'I_%��$tA������)J����Z����JgR���j��js=?IbK*O���W��Rr7Wb��>��ʜ�O��CMn��_K��
1>c�ϗ-V�Md���f�nڶ�[Ӏ��s��RU �5�i��Z��$�����4�]8Q�\_�Lz��<r)�4�2'%v�wM�#�ϻt��I@���0~[�#�۷h����_H����y����<��k����D�R;&�E���Gf.#�'	��d.���"ʨ�����G��C�-�,��wK�M����2B��1�_ǕT�'�I�3�<�u�)߶�[��zh��H�4��w��Ne��3o�ZN��=�o:�
�����(�i����Lk���f�/1�<���R�%izQ�G� ���7�8Ɣ��j�V��9!����6sr
l���N}��V�}��f����Kk6G.'QG6�Ά�$�u��6"�T���D'_s���������]�^��BD����r�T�Ɛ	h`��Ϗ{y���g16[iڮ���``��\�4b	*�ax"�4�,z����F���"=�F�i���1E�t��co�$��	���7�M*~|ml8޾�6	���� �Sޥ���4DM(�E!��yB���TTL�N�}�垌u�@l���K&��7*��&��uC}�d�7[�I0��R�9��_�ug_���'�[5&�`�'�#��̓v����p�D8O���c����ҀZU'��][��+�~�r!����=��8,��|��~T���G�X��/V�4�{��L��5�m��-��V-�`^��.��+��2�C�EՍ|w-�oZ��0�%�lJ�/?������ٿ"�j�3�a7㞴c=���V�ʹ�L�H�9�wn;�^f�|��
$5O��{��B�4_��G�3�;5��	��),��(y#����7���vR;u�ל�����n�����b��TC4�N=
�O{��h!Wx�e�E����BӐ��\j�����Sj��Zj�A�Ȗ!
⢫ J�)��`ly:�\��<���FJ����j&�G�!��|Ɩi�xOyzua���W:!yk}�>H&���רG��=�5��Ev)����4 k���2w�K5����Hej5��Δ�=�%�W�&�{j%o�.m��O������q#�u������ߔw]�?��ݛ�R�g�_��[�iNRI��~s���6 ~>0�9_�z��w�Bx�۶��
6��SJkivK��R����/ %6琕�c�K�s������E�0�\~��H�[7��x`��/ٖO%���ȆK����|�p��|��H�����BC�|ᗏ�M��`�cT�^�1!-�
�?�b��t+���ꃻ@�cSR����wRct>�x{Ͽʓi%G�\�XCRC�p�	�!,}�؃��<�8��B/3�g]
N�s�P`�|ͧ�RT	})|�<�w���+Wg��9ʂ$�K���^J�w��Z?���?���*'���l�~����gH:��IēSH���)�&챷DJ�r]D޼k�`��F�8.����9q�I�Ěˋ�x�y�_r�ަ�]i��]?���`�>�9Lr:K���KvI�F➁���/y$t_�!B��D�*�62�0��s���,��P���Gm���r�!�=o��e���������6��<On5�#y�A;`���LT�p�Z�<�� ��������ZP�<�sC-���1-��ܗ�Zj����-���yO��ױ�ބ��� h[����>�>��je9[�eR��ҁ�N��|����X��V��1ռ�V`����NC.𕃰Q5�UAA�d ���z�ZV�Y���Z���i�_�4jy�Q��)�
�"5����~Nԍ���A��B&iO���GM��K�����HW$�>�joWb�'A �����־��v �+',�+�/��xm�.�xz�����\�$�����
��}��/_.��3y;�N樧z�%b��5	$�M~J��hm�S�04b>�/J}x��N^%���
���jچVy7��U�� ^Z��x�9�|��T~6:�x�-5�����Ň���S��^�����z�U��ݲ�����=���X��A+��HO|�O�՜��&�qv���v'�F�t��nwq#��NU9q�zL ��c��n${��2*ZK�@Z����e�̅�^H\������Sm�� �д�~�^�q�'�mbN++k�Xe����U���D�l:�d��*�rJq�9�����кM�J�e%�F�c����>.��Z��M@��`�7�����U������=S98��vp���J'&%�՛��%�& S%;p�̓� ����a�q�p'��Q���O�OMɚ�	wzF���u:�c�\�{	c/��	����<��S�vd��9�u6KJ�!s�H�������m
�ɝ�M\�*�u쪆��l:�/�N9+�1@��܋���}W��T(�(9�(:R���_�L.����G�}���0�R�?f��V��t2�>�q�4d"�R�k��K�Ƈ�D��mi�v��4�����ɥ����=O7��9��yN}�B!�,��%�˵DB$���^R� ap��c�C<ұ�P*/���'<o��*��"�jR�2o]�T,����t]�>��&��� OT�V築��C�a�=�L���ol��2�9��S��z���K���y	���6Ǡ�.��܎m��+��;L�SQ�HY4�o�KI�)�����i���p������2V���Ͳ�=!0�C���Ӏ�'H�X���f&JT�v��Z��U�pN��f����0CfxO5�u)��Rȃ)q-Yk��������/�d�����}h���9��=
��̲�N��,L,;�;1��~�\����n��T:�lN�	tp؄�Z��w`�3W(��q�����|��B!��j�K"|��B돾�/;���"��_�Q�*+�%��*Hr���6�p�LS\ Pne�W6O�ϗ�\�X~� �K���i�F��?1�̀����<43��iDr}08'��&y2���׭������}��'�1u9[U��\�}|N2,���)�B�<�߂�~J�:/��G6��`���QXK�6�9���GG9d'��Hi�l���*�bJ�xپ�ݼ����O+5G]���ce�����Iem���Z]�p��ŌN�+m��q�3���2!�L)s�A:W��}�  #t~;�	�ؚ��&7��t���ܙ5@+�o69����*�K�m�8%��C�]��iK����vX��4�w@�����d��2�K1��V�Jqƀ?�N��5�&6²m�0X�����Oiw�RfA�sT	���0��W;�q�
���fe.E�6�R����v�^L꧔�O�. >�� ��r|��P=��M~	���eg��{�8j_K��r��I�ɴ7�O�{~��Ly>7�S��_��ָ�n:^�63i:��)%�S[�ؔ�N�̰�0�c������/8�r��M}��#��1��/R�?�yie4Bcz9�9��{_���#���먭��<��m�s�1M��5�I�������Ǭ+��Qv��
�LCm�+-    M����D%�"=��c���&�AӧYӹ�O��;]3��J�7��%��[Y&����ѵ���
��J7Y��\�R-5HOUyvu�g��cM�XRƭiO�d�ś�=>��ٜμE P!�ޞ��߆�BN��
�5�����Rbm��g���`�����eUվ��1��R$Q,'��gd������N+�+�?��:ݏ36���[���X�^M^B�9�YCfT��w�!�k��9�3��*y�i��*kR(C�T��JW�
�i-;1+u{/3؎��tf�	_zϟ��Gt������;�@^ף�:���N=�`��X;���>��~�ҟ$X?��O/�^�P����.Zmj���"��8�@�~j�So����	]�#��Y�ۢ-֢�C)��z#�}��+�|�~�mq(�7���k��4��x�yR6�PQ'�ݛ�}��T�Z�]�J
Q������6sv�8�@�$�Sp�T��4����Y��4=���� �_�;�lD,;�?c���JU���0Һ`��R�yڰ��̌(=�pkZbf�]`#��.�MӦXK��a>��o��G;+E��<|��,����p-N$��̛�uh��^��GJ��Iyf����@@�`CE).�H��dMǿ�`��K-"�'cl�,��k�-�3(+m]c9��A�C���-j28)�J{?8~��	�Wmڼƿ7>?�.H��ɹ��F]��X��pM!�^v˃�qS�S�^�`���
O��s�L�M�\|Z:z1]�����p8��%M�T(����L縎�T���H�??���N�fu%��7����N��6���)��:�!��r����������e��Z�K������&Q?)g{)L+Uc�e�x9��}'������sb�bl��(�,��u�R�s>Jノ8��r� �z�Π��d�9J|-�Ă�q���M
�k26���aî(>�\��W��7�!I���)ƚ���@Z��/�~ה���؝'�/�ђ_�!�
���qɧ����6K#�%��ӗH]1�Q�b�,�Τ
:w�<=0�ϟ�~Ks��r{��v�	�^\��B�}��8mm:r{�$}%qr�b�PRD\f!A�g^�*�Ut�itz�5�*��%�Y�?d�wNm�M�� �<E���Cr1L�Qd�i�6R�|���e^�`8���ib�C3�f���^>�����|_j����V�3E�����Ė�:x��N$>/���=�H�'��n0g�X���K��6�U��ó"K�u��L&�<f�~�F�f�#I��Y��;���߶(����$�c8��OE&��t�G��N�\1���X�"������U+�gaLz�}���$��名p��U��sB�#!���Џ��m��� ��R����l%i � ��Ѧ�~R'��{OI��:�\�mN�3O���ݤ��V��r1Ra�@� �LGD
���_���n�s~T�Vz7�Bwm��;X�� g�>���T`KLfk���by_�L�y[&���Ss���Q�."�)����왇W��#��y��o~���_ME�?>h�)6����`ڏb�|�л��
o~�(R�ycH�i7�l�G@i�e���څ�H���ӒS�i6����	�L7�m>s����\�ڍ��~��C����Z8��q,~y��7�
�[̻_�=q���|�e�Hi�q &�4[Y�q&�g?T���m{>������pbR�)��7Y6�~~��>��%,��'�(�&�۞�
�G�]#
��.,u�F?��.-�y|���d �ǹ�ѤcKn��{���l��/K� }�p�������U�
(Ё�L�P�)�,�4���Μ��I�/�5nQM\vR#$B�l�P��IO���r���ɻ��H/�~bN�sn:?����|����� �w�V������4wN�1�[Nw�ĕ����60^�QT��y����0A/�RAH����%�x-?����+w!~y���M �)���M++-ӆ�s5����!jG�u��?���~�1�FP��}Z��GO���Ay�A5�]{r6I�u��d��u.E8�O�dĞ�̈́��n߅ᥐ%�5-�i�Rǣ@���@��E(Tɯ��;�d�����Vd\S�'o�E&��$�Jl����/�	�cS��	��ݾ��+toz1�O�\�9t�nRo<�H�9IѱOw�9y��"B/�{u�q�/O��������H��\��@�������bE�N�h㨷�σ88�a>0]$8S�t\)O�{�e�?�mq�z��7�gR¤sL�O�v�R즈ɵ�����<�����S�|<�2JF�=�1�:OY�K�`�����i/�e)��YN:\I5���iO�U�(�
=p���=����	��i�ͧ4�|c�,��j���\@?ZzǡH~��n���R�\�~�9�&-:hN�>&���ߤ.��>�ϸ��q����Yv^5u�<�K��jѧ-�sο9���s��H���Yq��:�����+0fFG���"-LY�G��P'Ƽ;��BA1_!��&���Q�N!7W�/�P�~�1ΛXRK���|�����l�Rx����-:�%��(N}�ж,'�=��ë��Cs(_�7�2�sqxy�a�3�M=f��U��@vl�2��d�  �O��a,`$��|)rk>�+;˯�]���zq`b�����sI�O��>X���|=�L�CuM����UO���>�;6�9�Ft.]�J�Փ��JK��Z���_9��7a?E<����(����䰐P�r#K�M�q������gf�OR�a��"r�
:�E	�$�kn�>�3�y��%���0M�}��o���HGz��U>�G*�1�f'\ke��yA��nC�3��&���J�c����������ncI|�J��,�D߳�ń�����,O(%y:V"˹Q�!�ȝc7�lM)����1). �KX�Fm�k�?�L��-W�j@J�<������^L�+��oV�i�B�1V�T��PP��豣�I �cA��+�3[����n�.��q���%­�t[N����)e��Yt([��t�֖.TD ^�����לL�E���t�)�n;�$+��)�hk�l%��qIL!�J��5�Lޅ��}L�J5~]��G9bۏ��t�b�%���Y���g�(�n�L<�������[��kA��N'��'����ηd�b�C^8��r�/dF��m/���o��\����
)<�?mL�rJ���M>T�8Ѥ��W�Vl�4K��Q��J3}-_2D	�g�&�z*�0/۶������� &���ڷ
�9{��B� Џ����/ٖf_s�y1�� ��V<�Dq��o�II}eze=�<����3�d�ԧ�����1kME�H�LגI7@�DN���\@����Ne/��^Te� �w$��[�y�ki�V�O��&$���_&gy��>�����i�A�OJK;y7�y'y�s/�g��m�HZ٩O-5����0&4 �'��c�9|؎��9���'e�a'6�t����z:��<&mZ)�����g��'.<��$x��'������ �y#�#�w�rV4>�trZ~3G����Ju�+���`N�yY��k爖�����������Nb*{��I×���rZH�JTz_n��Xi6N.y�%g0a �p)��oS�A�~N?� [�\}���YY�ۛi�9'�N�`z\-�5ò�[_�[�D^��p���㊦3�C������xJ�G�TƇ	����Q������(ͦ ���85����1�NT�{3�R�uX�﵇�Ʒ]��4>�$����y�xw
Rn3y�皷y й;x�@L�P�˝������3f�����$���x/hE�d��盗֖>�����̭)��t���7��6qީ�����.��Q�$�	����� K�L��[����g�G�|�7�N�?鹖���`;��ߒ���8h����31��b�~=sYg�����I��NFhV��z��q���7?'({�?��;k�����%�p�@�6��n�8�j�ѡ���m���*!�B�Nh6Hkq��
4`�T�6A����RkYb�L6����_�'����3�z�<�B�B�%�    ��[�֔%>���a��
1��>n���0xG ��{0T�A��5�5�ɝ�Wܝۥ����\~�¦-�)�'���!1v%)^�)!��Sd_��pZ'�y��6Ƚq_��0��Fr&����y`���\X�/!*-S?���Ri����`"�2���w������%'�5��J�y��3=���ٕ��5�8�I��>�~ �o�}B
~Zy��ry9���Iw�t�������7�8*s�9��7hv~5�����6��)���Yp	��)[,���YlVq�.�=u�G	jܰX@�߄#���[@��o?ĞԽ�Y�|H��n�!�o~
��wQ�[w��݉��Omr���ƖBf���� �f ��~䯐J��ٺ&�����1jW7)y��D�1ѽ͓�a�F�x��y��1���N�c�Nb̟B���A��$��Z"��k!G'��ـ6����XW�JZ������T��
D�n37-u){����	|%g�L�\��N�c �Z���5é��z��	��j`~q�e�m�ǉ��D�3Nc�\pZ���),K�&u&C�1�?���UY��GQ$����d�*�MR�p��.�"9��9�����3�I1��aI3t|h�>5C�Y)K9��'�s.�3��0�S?�J�c�T��P���~Ȯ 7�Ji���'�h��wI`|�Tnx�N��M1C��p��)1ʇ�Jo.ȹ:
���+ Q_�η|���5�+K�`>�&���,���2D&��냕�VZ�^'������Y]BR2���E����b���KW����s(��utj��;�e�I֯��_Ëd�5{^���j�8ʉF�mj� ��R襟���(�+�l�,=�״�W�����n��4����×ݪ� ~'/���|��@&�q:�3]���n+iM��L�ic#�
���Gx�8�M���Iz��!���pJ�֔)уJ^����-�v���H�qJ�B��_#��������d�aӰ��rF;�B�^F��L�@���Nc�����W}%����G����]L�k�,pXF���~�\S��>3û�7A��L�8�U܂v�~��&!�3�LD&��r�;St\eNE68��;^��,��b\���f���`��G}��=ַ8��p�������w�aa���a�@�᙮r� �b@���`6�܁l6���l�,�P[���1��D���~�H8�G�]�)��SZ6�����r`���S�0Mr@����Bno�G�	�ib7|��Rl�i���)�����f���ﬆ��H�zɐ� 
3'�5iD�BJ g_�Kzi$�K�U��-��݄S��J�&e�(��'�Ҷ~�U>��	� s�J��Þ�0r0����ӭ�L��>�i!�%'i:M⹕�-1\|�����R�(���R��Xͣ�J�(���:1�X��Y��F�� +}�h�$���,�d[M��`�����:}i�:����7N��O'2ۯ�H�����ߓ�.�4��#'MV��/�G�ISABS=��y��8�k�wT��6��qPo�j*L������?�?S^jh�������|	2Ij"GG�UJbb)=e�������d73��a������iJ�1� ��ٳ'>c�P�����ɛ�K�/vh7�9Y�|��)�~���8��`s|Sdמ�,C����>�|D��C����.��g`�� �C&+�7� ������eɇ�$����8��13*I��w=Q6���dx,�����=�m;���}��i�byf�	�%��)������ڲ̭h�$n�����RO�9[�B欋yw���������C�[Ȋ��Q�$�.}���"Naq��wN��f}T_�W��r� �%�9ْ�R�G���.EJKsu"���T��9w��)��i�9�=���gn_⣵n�����Y�Q1t!.�ckR8@b�(��j'���;+���4�C}�w�=5M���̏�$g|�l7�C����s�qړD��<`�Y�`�>w2�C�� [G�Լ6�j{|Ӥ�e$��6��t�F�`��M�1-ϡ��}�� Q��nB��D�\;-C�����2��g8������C٧��Ӂ��U;��<4a鯪zRX�������aB�de&ڷ�-�B��f�ԉ���wC�#G�AV?SѮ��ʈ9��+��'� ��s!S<R�dL���j�A��ҫ���X���<ݹr�Z9d���y'��T����e���[Ѕ�P�T�{����(�+aY���ӥ��>�E�Sta���c��ZS��9��a2vVW�h��H&&���4����鵞#�"˦��Im����%�	�<eb��-��㙠�>r�M�d׃&gƖ�O��0�$�|U�AF@�<�m%@�U��t39�G>�3�h�RX3oL�#y�}��`��-������\��P�oy��K�68�U�a9�s�pI�v��d nwV�o�xn
+G��YX韑��at΃�V�|�E���<�������s��+�J1+���;�^F��1u=+�bg��Y��Q�����%�v�Zs�lE���70�.)\q��*����T\Q.�7n�;��k�sv&/}bo�����!�#�nD,@WK�[�Ɩ&�lU�߅
�f,�׊>�J9�|	�r����3��^��c�b�@iO�8�_�{�H�����Jv�.�ʸ�]_q�����(�P=&V7���yns��~�+s�Ǐ�w�>ZG�7�
��=Z��8I��wb1�	�������!*��WO����X"��w������!Ur��t��8$�����?����䜳�oyCM��"���J"�dd���6^�[ ��Nc���#�qI���7e��tQ�����f3)�NK �%��R4%�vm����8��S��K�I����J��OSD��$!��i1�L�����.�]�){�4�����_X�bߑw/������Ϫf���Q��g�\��\��-K[��78����_����/�)3>�Υ,W��+����7�4��H@�
�]Jp���fÞ�1� S�頟�v�rK��v�i3/�:�Ӕ)z�='m�}��~���:4��Lֶ���Z����O��-�@���7^Zy�a+$6�������|(+V;��a	��M+�Ϧ�`���9�ģw���ŏgr��̭ȍ.o�܁�Fv�n�4e�<��Z+�̨�Ţ�L7Ir'����v@h�HX�(N G���B���G��w%�:�X49De��F�o�����.sd����; |:�i ^?�B��K�Z�h�3cK!��hK=��i���x۾I��{P_�)T����Y�ץO"�f���F�k�`R<ץ%4�{0��E�?h�cEB�����������H/`������<*�m>&Ïw4�N���Q��c���Y��ٝ I�m\I~���RJ��u�Z	�?�YN��	����}$f׼��O���a�-�|����9��!3H��3�0�x1A��.'����^�6�O��z�%�d���vcOY�]�YC�^'��	�v1�	��w�
$�G�;���/F�I�Iy@Ե4��N���;{��y'���I����o��1���7�wb"��ԑK�t����3�ٳ�2&���F#yca'�>n9������ӹ����(����e��b��Pl��t��_�D�'��]Bb�Q��ic�m����z��B�4�Ws<��jEcS�\��)Æ�fN3G*�'��t�_o�hQ�}�O �{�Y���c�d\�r�(�&F&J�-���Zl����J�E��+��9)��Ԉ�a���3��&DjAR�N،X���}�췫�z6V)�e���Úڸ^y&8��w�*@�!��m���T#]��G�s�pS�R��Y��Â�Ŏ�[�0��G
<��'�$,ﳅ��v{�<7��j>������=�ZO�zr3N�z�Ie�=T�����[
O�
HZܜ�~������e���Oⷻ��%�B l6���E�!l�lLC�-.�><��n�����\;8�;6\T�����ҷ�D�>�E�������G�yMf�9�$Uv����4��)��&zp�H    M/�=h��#~�F�k���PL05��F �G�������PP�+ʙ�YN��ݟOB;:Yr�﫡ɖJ?��<���\��po�*䓖��9܄��3�og���������.4pM�ҝD��'i�h:�kM����tи�����(X�����~�F�n�^�#���	9-���!�{�4��k��G����ń ���t����2+�w����-(2Z���2�n�u�]�SZ�Ԧ����������]8��BU���%�����F��%���cO���#Z� �}t�Q��>�§�~���n`U�%b��-��l7��ڧ��	O�,X�G�5�w �K�kࣘ1W�߱�^w���-���JrC��ߔ�%�O�o�1U�����*��A)�pZR�&q&0��喦�\[���'Wf{�6X���ʕ6�ܰ�V�Q7��c\�Rz�qh�y�e&�5�0��tZe����3xn U6�	�H��LĤ8���E�L_�kc�yA\�'����Sπ�1�-/�f�~� p��uu�+�7)i��M{�K����	���9����s4�w%�B��{���(U)���GA�)S�pI�[����ɜ�@�V�^3O� ��Hby��]�<F�F�sىc�)I�Y����X�o�ߩ���3����9�͇��L_�ƕ�������d�3-E��z�����%��*��i n�P@;#�o������ ���iԷ��F{��yaLШTC.�pΥ���.��M�}߅t����M?�I�
��I}�n�
sb��^���`b��e�������h���nI�8ٖ,��7�����Qx�%G`H7s��WC��5����KS\��2�t>�$P�(�'}�V�*F\@���R���hA0�7�֔�4����+��)�-.��i6�s.�;��AB��T^(]�ȟĿ�Otn�\)�V�5�"O�y?����&v䟆���h�!�~�7 L)�=9�M����4����E�L�v�-������Q��L^
e�scM'�� G�t�c���vl� ��xm`
� �Y|l��~�ԕ�2v8'j�|�������I��fqğ�o�.� B�;��V� Ų3��2���@5��=U*���QM�m͈����y��'� ���k�%�F;j4ǰ��1�w���ȋG���t��i
-�߆ѹ��\G���0$���qI�u�,(�hK�� 
���V1�z�8���"+��
�+��I�{���4f�Ū�r�6���a�����峺~lRآ����D�a���J��c�A�o�2�_󗚉���[�_��q����%��J�|*����d(����*�p0)i��T��yN�T���6ơ?�0I�����)�1�Cӭ1�|=����^����6)�C^H�n�a̓G��{;v6�s��a�D(�Uz�P����'_�R0�S2<��u^�I\�di[��_;�|P*��u�\k�|����ÌOyk�%���i��A�K�m��[�4�sG�M�o|?E;]@��{�� �ql�ӴS)I�y����4�,���äG拗h�7D.G�m2�-y��F|�Y�N��PB暡$��\�އ������Y��'C�ׇ�<�/&�ɦ|M%�,S�꛽Ḻ�V����������:fn}����H���
��NӱL�gh���9�Ը_^��s�r%��d�ڑ�j�$�.��<�dJ���q5�t�����}Dӆo�ݩl�" |���8����=�U�.]w�U�� ��i��R>N���йS��π%��Y�|)���T�Ѽu��M���ҶJaљgI��>�[!w[����C�nJz�fr5fV��d�T�$�W�N^C���:�-�`A���Wq��s����U�jO�v�\�,[���%l�"�����&~�&9p7���AS���ǐv��S%�_�[Ơ"��a����u�	x���M���2.�yx���oŨ�I��,�~�IY�s�	���$,�..0����ڲY %�U�N+�u:J�d�R�n������:��E��/���B�=<����7�4���J���2���N����<\��EA3�"�����6T��C����o�H�|���2������iRҽ��[���[{M ��(M�2t� ��q��Q�N���_����:����:O��9���U%���&}7�b@Ĵ{�CiU/
�|5Ti@��B-�"m/W�w����&�u/+wk���O���?� E�+�� ��*͢�t2.ڍ�V�I:�B�q���G �S�-pzM�y�����V�O8�j�+LY�R�Bu�gNp:J@`�H�k�t6A�k<.*��$�����tCbp�EH��7M;�r���Yb���?�Z���I�n�g��梐���9��Z(���^2��cr��zl���S
��a3�4]�|R�(\�5z���v�t��}0��/��d�'�W�`q'��L������t	Uc�+�u�L�V�̀�aʗ� 1$_Q�HY�N:I?�^Ze�V�^PX���Ú��&��MD0R�0m�����5��6�tX;�.E�/v����E;�ދ��z!?��K��^��0D3���Rߺ~Oe�F��n��a�F��pr�2J����Xr�ЊY�iV�)%RH5��YG����~S����Jbϧ���^eXn3�s'T
�$^��L��$뤃��4 +��$�xQ.v���1�$*/]g�"/�р�����Kl�a[WJMk�L��V�vR�ϼ����1�����n�w��[��D%C>ҏ�JW�w�~}�h��7-?�4r�)��,3� b.�0q:�'��=�.-i�Dbx��w�_-۝똊"�!=u���w3P.|ڣ��d2�Lp�J���2����n���KC�y�����rR$��#L�z��{
[D+s4��j�Q�Q�"Q+�A��m;�沪K������F�2���F�h����,�_��3��ςp�O��ǻ�P�����b\��2�;/��	*j\��t�����Z�d�~Y&�� n��Le��i��C^��u�O�ɶku>客L������6n]�=Vß���&����%�\r�_��Y]P��>�b�cL��OJ�|�8���L�~�H���J����<$!Yh�q��T��"����:��+�@����#ϱt�j���=�i9���� ��x�vX��.��TǤ=�8��	p��-]H�A菴��ǔ`�@�%ej�¾%!��L��:TY��ŵ�6�I�M�E�*��8 �'�G{�%��M���'V]LqHTH]5�J��F�5�$�r* Έ���!�K�q�4�Tz���p�����2	��Sl��&��(�`�,��5M	���Mi�CqRR_q
}E�ŧ��5��t��/�ۈ,~x@i�Y�g=8�{1H�I$7�]��'�ۻ����?�?aɦ���v�	O	5�yƎ`ΐ���<�H�ϗwj�܇mJߔǩ�nU��Qj���7l�M�l�V
�)�ƩSk$�s3��}��&��ޝ�LnG�$^\t�BU�}@N�h(Idb��wX6�d=�F���#j�o=7�釘��!��$��¦�xQ���R�$SV�e��4.�Ó�2��z���%xa�݋J��Qg~��Al����q�5�����R�m5�k�^;�^~)cm�.�ݔ}O2�`�}^顅�e����e?B?��eѝbj=��TeѼ��0i�(�������s�D�_�m��ot>��D����Oe`J�d�	c/WM��i��DeU.d�>tv��H���_ΎӐ���"��3�Vp9X�=J�E,��Ayr�t9�AGr�&f����\L����ѓE����bt%6���+:��f��2�-���G�hw��}��*����� /Gm·�������`Չм�sВ�Ec���0�D���Ǝ�Üt.��U�n�7��- S����b�"�,v��&)#��Y����z��Es|�K7/a%���@��hkge�d���*��")?�o�S�����	Q��X�����ʒ:b��.c:���B����#Y�|.�<���<���߄���O��Ƕ.��������{�r��l�}����:���e}�Ĳ�?    �i6�����]�-W|�c��8��PPu��� ���Z?�������礶�~]J��1uu��X�)���l�F��?p �L�8~�b�7��0DM`���d�[}���5�J83�y��eIX��f���FBuvYt� �o#���nEay5E�5a��.�0�J���zsz�Sڞ6�Â���6��#�5/^�s�YJ�]�K�i&�+�$��Ƒ�Z���h$���4�X��Ob�H/1݄�����%�V��Ļ���m�]%$���<�x���N�h�F��������`%��^��V��T3���0�["�-8dF]��������"��s������ыM�i��ʏ�4H"%&�>(���h�������50�2��L
=jk�^b��By,��g�fE��N�;"����{��,���2oz�{O���y�Ek>�	2�Oo�\e�ޕ�:��T�k�i�~��ئ[H}����6��=Z�Y����g� ��d�70���Ⱥ(8���/��4�M��])���ϓ��Ų\H���]��I\D�;���%�y��S{f�9X�fr7u���*g��E#q�J�:��+5�J�z�k��z���
�2(��Ƈ"����C�����L�L� �����]��Y�����X�>,��_�h<��܃�� _��4]J� b��=�yD�q�r���R+����TD�6�WN�R�/~`*�t��x��p#���v�i(�7��ޱ�^�	y��LM�82�t�g��}��,O��;��) \�bV�y7A?�}�j��462aJ��K��v�mˊ���i۠�v�����8uR챖$׾"<j��J�r.�&OmH�5�)�ҥ�Z��O�u>���wQ�IYqPIW����BI��꣐�`$P�˛�����A����(u<��������zn���>��Ԣ�:��G}}�I�<+���O=��M��'�����!R�,zzBPJ�4��傸�3������6���v�Wf��Fъ�}KJ]ޢ~ϤsG���4i`�F�@~9�N��������>(UG�*��-����`�c\��i���;���W�kr���Z���-sЉ=���:V�����̛Z��ڃ~}J��Ԛ�0H�}T����1Nf�и��ao�0��r�M>J]y��	o�'٣2��4�ӑX�#q�|֕L���;��(In�o���]}2�z������0x��P�L�B�7al�'�ͫd�z^_�\�i���$/χ:5�D"M���ۺ�M%��hlwR��
�7o��a�Y�\��g�t���}yS2��5]��S�[,?�!͵�,���-� fYhU���کk��1DO��MF���Цr'�K��ԧΣ{q��������!TӉ�V���_Ԫ7��"�cI�|F,��I|�^ ���?2N��Wx��?���?9�/Uv��ˑ��4`BRxZ���d�K͍q]c3c��/[
�UV}H�'��d9�������aX<�m�x֓�SL�Hh>� {Q��Մ|(9n�v,�0QLC����~�ٸ�M���{Te�AOT ��'KTΪ&��A���w���(_�nep��XYj�>P[�����w� $����Y3�5Ȝ���(3�Tǹ()�r_'ß�qO4Uy�RPJ�ǽ0�+����t����?^X#ˍ�&I�e��d*_'	���w/]VV���踑Sՙ#�a�/dgd���4��r��Q�M����N���&��G���8�b&����2�K�X	��z���K4(���F�-�g��WF�拭˒���q����$A��_�xM�J;5U�ay(5��}��[h	̶u���Ȅ$�c4���;�ǃ�;k_���0B��THx�����!�N�so-Ŷ4*ka���+��F��?������1�K:KX���s�SA�hN�pB�������=��וKKp��SK�
r	�5�E�6]�1���=�u�vʹ�(
��ov���?���?��ӫ�?Dq~�{����{�绊D�<pㆎP��5j�|
�<�2��O|Hk�r�i�rX�ZvN�k	���:�T�)g��B�Q�3�χț������@%}唴�ʹ�#/u�Xprئ�Yɞ��J�2�Ǧ/��j���"�h:��s��MW������s�>ϰ��4O�9Z'���Vz����\���v;%����]eY����'}>%��(�<�F�hM����Eo1��F8ʭ�����y���r@Nr��Q�Јy9s���	aX��]i��| ��Qb~
�CtV+�?GEJL���24���F]ҥ�F!�y����R��C_|Ɖ�F+�H����N���L1�	ni�A�ʪm�[��b�>�)=CD.�)S��������gs׾�#Q��_��x>��Z�@�k>���(������˭�5u�6��5	��dֳ��@��Q�HPs�&>5*�5�'���N4z���w[LcK_2a�t_Gl�T���[��B�5�yԕ�=5[wǖJ s��%�fa	k��1;ǔ~]Rp��^�~}� dB�ZrM��؁]�5-d�R��ND�9�j���"F@��NL�Ҝ��!e\��P�c^	�	�xZo��d����s�˄�N2��d�����~�~7�E��u9�3ϜZX�>��O�$��������K�yO|��6;���[HrD��1yкn`�j�]ۚ����>l��-є�����U(��B=a����f黸�m98M\���گ����q��
Cc|�c��EϨ$#�w���!��/-�|KKӌ�-K�K��;y����W�@S�<;P�������!A��[�ddR���c�I�j�@��3)����'�d�Ey~�z�JgT��y�%��D0�q+J˙�	PT�� :��*�&��n�lJC�ん�a�$0����U%.�6�O�͹#�t�k�K7�����`�H]�$��39"��kH�mGg�ޏ�,��i��jΚ��}It�,����ҾY/k�!W��HE����:M��7,�%a1�6ώ���Δ�m�\~ν�vCΎ�u墂���EL?i���X34�&ǃ�2�6�SM�]tΰ��s�O�����S�����#�YU+�t	��uNu~��'W71z�ر��מ��#��%���4f�����nឺ17mOA�=
jgO��������ڏ=3�w�>��I����@2��~�B[yZ�Z�{q�@L�Lҭ�3�~ɮ��S�j~!�6��Td���:uq���0Ȃu�<ϱ�����GB�g�a�I��L��:E-������hV��v��d�Fs*��3�3&�����G��t�.���oӌf���D��S��?�����3�`c��Q�L���{�a��#���%o����O9X=u��pO�B����	XhH��z'��#�q3[̶eƒ� `��\�J4��Q.������qg5i>����n��=�Rzy;��(�i�����m
�ͤ�0KM�܏�c�9�<�y�Y��a<䝒R�90)��k�/�%���"ii����'>i���3�DهZA}��l#����:U垰6�\J������--��)�&0#���K�!�d�6)�.F6kB,W9�=�a�����t���	ֹ�%�6(�>��S_,)55}�c� �&s��S�Kz �Q���|
��io�H*�N�ݱD/2�%�e2���[ʟ�O��d�<�~�����ݠ�Gġ���f��.���/��A5H���m-3�p|�|�ހ8��@�ɩOyx��ά���RL1	=��s�o�;�&���q==@bij��i�S!��s�I_jZ������蓼>2B�$���25zj�ZBP�;�y������Р���[����U�Ib��U������+�w��	W�����g2Ձ�h�\�]��mٰOyʍ�K�Y2Q��[�0�a`h(W�u�� ��~�c�����[�h�.�唚��x�=-��Q>��eyϊ�M�"c�Wk�"<�I�8�L��Lb���q]��n��Ÿ�(!�dW̴o���    ;�c��Q��ռ���~��@�.V������o��r��!���=�d�niu�˿�`C�H��1K8�M,"N|-��s�z/9��K��m�b7ٜ����9�%ZJ��#�M�(�WA�X�F.�ʨ��N�-/��?MF�:'�B�j��� ~W_�J���f$yFE��\�����M��N�J����v����K��R�ׄ�k;*�".��������[�(��\��+�,�u+��J.5�d�2_i.fU'*I��I���'������s(��|�.á4�R��Rj�o����gZ��WO�n*��@*+���!���x�����*�ǟ#�Dܞ�������̥zYn��ܜ+=�|��C��b��Z���dy�<\5�#��3�VM'���:[7%���>e)���nj!Sy;/�V�N����-
)�M��*m���;) >���|C �K8t���lOXb��JW�O�h���u�&/7$�Y~�NN7a�1 |ש4���Pq�ڶ�L/���
B��˰�H:3��F&�[bV��Y#����XͿg���J��]���o<�O��L�����G���I�/c��bp}��m3���ϴ��o��I�6'(�x�ޘiS_?&E\�B�DR̍�ȑ� 15&`�d��F��!�Ɂ2�|��_�1g�`!�&H}ki9Nt�R;+���šX8�$=b��rbHb�/R�}�cxN�wɝ-F~c����2VΗ��C�$}i�b�g��&2z��)�L�j@e$O����Ț|٣�4���y!'��.yJ�>�v��;-��s��ӷ��8	��5Z|��-�Z,o�!v ��F�d+�J2.%4���f�A�tQ��{�ت+t	��9�@���o끧_>ԃ�z�i^����H�w_:SO�H���������5�Q"#���f�it�p���!�
��̧��Je+e�f3`��zM6�S�Kc����s�T����_����-]1��K�(�As��3)�Ð�P���������,iҠi|�)��?�B,K�l>���_N�r�)J��x�C����s
� ��sT�d�HJ�A��#�&*�7��� v�d�ϔgFc�d�s\�o2~g!��.�����$���+�x�&i�'G�p�&�F�)�Ԕ�R�[�vA6��@�y��T��0��)���Ԅj�\���J'�Y^M�1pZ��%�G����ɡ� ��:э͢/�q�����R��Ox�}��O����	�}%gG��#J`���ȓwJ���=<)�9�ݠ!SzQ�)��p=�ʣ����z��G���E+f&��srMCv�jo��ߣ!���#s�x'G��(4��Xr��|�\p����G�Jѻ @��7��	�ms'I��J}&��9���/�����s��,�����:R�r�Ina���ز&BM�@��_�\7{�e��;e} ���i��c`0�@�45/d8����W�k�t��ӥ�l1<�~A��oGn��Q��8v�8`� �+��^"p�	�0����
=�;��w�9'w-HS���Nh�b*�j��_\YX�t͍��^�:�]�	(��'�"�x��b?��Qq���Xy����I���f��	i�������b��@n�*��I}.^��r(��p��~�V[.'G��S�&
G�h3�{-��rS^<��-�����{Z1�O��/�2�Qx�pO�nb�Rm8���{����2s���Կ}�r��N�m�j�zNd�y�M�.�MKj�T���B`)U�.��f�	��ǋxX<%�������d��?��RTc��O	a�D^f~���[\cm>�l%+_�!F=��C�,�&؞�|�)>�-�ϱ�CY�0[�IJ87*m�q��%RK���T%�g�x�#$]Oz�7���,�nC-1w�$�4�O�d0���mH��/,��s�T>�ǁа,Ee��V�ٟ��������["�癄b3���n����d�k�W3h�7���J���s�5������AG�bwm�lʇٰ��f������i)%6$���lEO��"Q@@1<Y;ʁ�`�Ό��:��J;�A&��I���h����\N�޻T���M"�jB2|:6�eiJ�z�U��FTf, �P"�e�TW<�L��TSS.Yx�h����#�;�IN�񙳧�'���(d�ns��� 3�0��O���Ȑ��{��DC����Y��HeX�w{��w����+v�>�Ui0rӌ31��)�fe������>k�|��ɭnK�7ݤ�Rh�4
 ٕ�}AZ�$PI�͉a�1�63ej*��+�A���z	"޾0��4F�i$ �!�9p��L�I�/i�'�p�.�$@�]��(7
3�:�+���/�`۱A�qBˁ���5��1K�K���?�q#�ߩ\�p�M��;�:�f����F2�(��ڊ��A<I���T13���/���ԓ�p%y��%�|*l&��q� _N
�f��;�u���%.1��Vlw}.���M�WΫ��/� �(i�+�C4�|�/��_HH����ԼqM�K�B?_3�:���=�
ʩ�H$] ��3yF0җn��)GS��V��xig����Rx���(K���7�R���τ�lY鍗$aM���oީi#"����~���\m��b3��E����dH���L���'0���_xF^�[Y`Y�Թ��GW�qY
N/��=_%����8>��VzJj6��X��9��`�����R�I՗'�0k�}Ő�
s>�X�z_0+�6�����$,���b/	׫M���z��:3$c�����H��0�,��dX�K;ґt��8<� ���L���EO�!D���e>|$��q��v� ܐ���������Sb�Af�//������x��N0y�Gȣ�)#dm;jk�`����y*S(�<�`G��Xm g�)`6Ğ�i4���U�~�y���8��_�.$QV�%梔Jǳܵ�V"�F3	�?�V��Sy8F`b��73~���\u��z�^nj<9�2�ܜ�^�Eqʂo{�j܊�\��h���]�����'��T���?*f��G|�A	]�Ή<�t�ci�i.�?�K���g`f
���؛}܏
��|����t-S�I�}1�%m��Z�2��]A�s�RO.RR5��j嬼,��� Pw�a��F�`D9�r�/�h��&\*����d$�Oi��kZ��g>}|XB�i`g�'ɔn;X�L��~enS療�:9e%�U���EO[����E:�8�n�z?�1�S�^�;Y`̑r�a��;7M���w�ySW�۲�|�d/_�M~{��[������q GY��a�&�'q8�A#e>�&vv��f(_�d����Y�w�s�:r�S
_?F�}�Tvf����Wh?�J�U�HPZ��T�Y	��3�N�=ʦ颭����PZX}� Xk��n]m�d�"o��Q:X��KV��]�aeH��9>�G%���59daO<!|&�o����oǜ�2�|��o��nU��RH�U�{�[����,;��떺6_�����L�7�n��`��"��!k�=��0���h���g�8��s -9���-��ϟ�i�x6��u.߶s����w�}��d�dq�6��� U�[�L���<h�ޘ夋2鰻a�Z�$|޹�X&��=�k�9��ݬ���Ǿ�{�����K�� >�����F��}�@��������y�j�܌&,�xn`�i����9Q�>n"����i��� ��Z�k[��9xW��4�򔟗$ �J$0�ܨ�<wĐ���R=~A�N�l#�����רf�d�K�%�������_"��1�)}w�h6�MCJ���}��>�y(5��\�i�q���8?�j��r"z�GjKA��>�>�G�а|���t�S��`:��y5�l��_�S����������c{6����xc�س[�� ���،D�0t�5���2������׷��Ȋ���!;����Du�%\��gj&��(;)�k��W%����L�yC�u�1)���	��Sz0����M|R����[�ޖ}2�t�@��Q    Z�-%+�6�d�������uja�7��XE������Xm�.oR�%|���o��������s��(�f*���ۉ�@�TY�i���s`S�r��,����'IG�D���|�N�G�3�d���~�Hr�d�sX�F��C>m��+]�4�ic4=@����ֹuRO:�R���v���r-�ГF<h�ee�b�4�ͤ�۞d�"t�ɖ��,&��D��m:q�'��7�i�����Z_���5:��%��5�����Q/��R�?�r�wS�'	�LZ���B����� '^i�@�S��^ �W�z��A&b[J��I	-�3�?-�8U&K�a?�r�^l�\�E��{s�h�D��u�J�g}�`v]5'�OI��yB��l�'DF\��&�PO�Ä.�G2� #�0�&�T,�Y�!�3�I���µ�w��)wX���Uh$��i�4�%h�Y.��S4W�~���/&={82TړB1Q�a��\L�����A����H��F��a�߂J�3#��&�������Ys0��4�\ٝ�&�6�"��J�9��4S%����J����`'�^	��ؘ����XhXR6
M����B�`:����]O0�y/H5~e�-{�aJ?�wZ���;��?����>�qa���Ч^�)m�q�V.T����<�e������K�ĀQr~T�gQ�F���`jtȀh:�s��Y�67���	&Mcq�Pt�
�v�f��񓡓L<���':��Ж��%Hq���;� @J���Ҹ���6����d�����.��1);�>��:e7����-�;e�#�Q- �'����I�:�Ȕ�3zI������Gk��٭�A�/����L_$���iy˩(jEř3����d��l`�ԮF�ӑ��.�eY��BT&�(��I,a0���˿K�-(��GO�e�Es2vOt~�Ss�!$z;�T�ŸI�B�iH^���N�ޖT��5_c�J
T���^h>�Q�9�R��)ة-�d(;�q���~N@�3���t�D�&�|��3Eh��ظb��,HRNZMa������$�T����JqZ�����Տ�sm�}Y�Y9�5濞�)�j(�%3p]��hʞcU	d7�Z>��2�xJ2����M��t�۲)鑑[�Ir��u��h��#�s1%ٓ�ߍP�V2�b�MU+�е����;�96����s�8�t�馸�^��ց{z��M�$���`͗������u~N� Y���^��NJ�x�ܜ}?�6�� r^V��c�p�Ⱦ���Bfk>LX�R�_��1mD�F)<�z��i>�=�������g0Y�a�9�O��i�`�8����v8SRدr��-DD��t�w������J���m3J�>�Q�{�E^��y��LXb{Mkr�F,v#�#l�ƫ�����Ӹ��J�\���姗���"��i	�U	�āDߛ�A�@��8[��8��@,�&5����t1r����_�Fӭ[98<�-�_�̧��vX���+"�@�s��-�-�-�nk�+oS����ꑓ\�X]��S�A�ICBp<͐r|���R�;;�˙k;�m���G����}�1>�C:�b�����'�ꂩ�U{�й�S�w����	��j����DnH/%ߓ��vb�+�G�p�`w��	��֩�=���:h<�U8�����Rφ��4�����陥��1�G��ɥ�K�vFl�X�0��XMy󸱸޹q�PV���7-Υ~��m�?��.��x��#6xQ$��y�����@�Q:ɿd�����'J���ǂ�s���'�ܹC7<���h~����^:�@"���su6Z'NZz�PY���R��c>�����q���w�ǝ���A:��RF�Ts:g��^|&��E6.�=��A+�i8�����H7�����O���9�ϑ��ւ6���Djn ����>�.��^�~���+�k�7m/ay�^�zǄ����g��D��b�������Ѝ%wI�(*�+�T,T!<��#"u&"��M%.�0K������J*�7-�w�&+��I$òy��Ob�]w3q����^1��>��!~��)���d���IO^*˦��Y�%��lގ�s��X��H�1���N+��NGZ1^ڍD�_�2��A�g����o*�i0:��Ҽ����/�;��6�ty�F�)��f����-?�T6����+�_j�/�ëv���p�nr1rK�&��I6�9����z����YZ��8�l�@vm%���d`�`�dH)E󼚓�_��K]t!�@�Μ-MC~e�J¥����>T�)&�Ud/��� ��Sq���GnY�ڞ������[/�pūI�n����Y�@�(�[�S��b�e�|��������[�>��h�H�Ǎ+���<@K�i.��{b��,%
_�[}6�;�]qf4K�%'3�5�n0a�A�q,b8͟︶����|.��M�yXG+�P�����g~+5�]i+�1�I@u%-�Gڠ�ӟ��'	(%S����Y��U����R�yi�)P�7�&`�f;�ߐ�$�&���Zh e���Q���bS���q�s�$u70��?}j���.MW���[����<� �:����K2,�VoHm �0�(�q���#��r�f�6��}+��TZ8q�������@c��k�T���!���8�.%��r|,aRX���;��m�0h��xn%a���_La���Ì�ۇ��E��k}���u`?���0�#���Z���z��˗�8ICÍ@ʛ��Cm~E�g�JL��0��a��SrL��"�]漤�i��َ��τ5x��<-��y��*�����l�
	�kt�O�KF��"ve��;�y̥XA�v�~�R�� �>&(����T����K�S�J��TaA�aR���G�!~���3��J���,l�9�<��p�ÔJ.��|/���53����D<%�C*oI��؀#S����$�א
��Pr͜{1���y��_)(����4l�SODd�p_����IaB�ˑXǘ��,�[d0�'�_���:qF>��S�M�X��^����n�)��D��q,碤����v�=qY������
�T��S���Įs�^ԕ�@�-UHND9�=)I�I��Ĥ�v}ң�xY��l��f�t�,�F0�/m������?�u<H�ҡJ_��R�>�����c�`��{���Y6I��`ꊓ�i�acW�7���/��0���-�{��}i9�-���w4}kt土Vs-K�f�h#:��kv'�o�Q�ͼ�a�|ɞ��X#nw��f2L�JɍlCR��9%l<1�'8TnfI��������`��t�6����$^	K�S��q|
my��W�G�Ds�)k��x�^ݾ=��'�ͩ*����B������\F�:�h�lC^�e����{:p��ӫz0����I}����{����H��,��/��D�C7b��W/)�>m�?���6YN�0��m}ɓᛚxK�8͒aX�<�i9�<T"��ր��K^;i7$�u����c�`W5���܌%�.1Q�!��UA�RwZ
�1�m�"F h�q���൒W�5}�2���}�Sa�y^)��x��UP�	�s>؏��H;�O.�x��!�b�����A�,*��U�kY��Ο�a'���Ί,���y�$����
�X|g�`���\y�[�'m������N���ԬcJ��N���D���Kf�y8'y��|�����&5����;s��=�;Fȼ�tT�p^G���^hs�ݶ�`p˗�9�t�ř��`'�'��`�:v>s�AW�}�9)Gs�,�����[��w���ԭ��<�����B�O�E{.� �s��'�!;U�è3�J˒K�﫨��P��Q�!�&X�K�O�o��-1b�-�,��;�+�x3﻿e/�2i��]�l�e�)ܗ?�e��5I{EAy%G2��Dw��g�TwO9L��O�P�����>�ib�/��6~���r?%8�q}ڒG�f�O�*yHK��D��g�&��X|S 3  ����t�ܿ۵��l�ӿS'%�2YI��UM�&�;&���2?�"�ŜqTx���g/"����!�t���GD�<��j�T��]�r�`�	}q}xrnQ��s ts�[�^}��I:R�Bf�)���{	�2L��0��Ϝc0�����=�~猤�1��G��;�ѡ)MXZ	z`;��v�ܼXr6%~�N�"]O�
ǥT:�F&�W~�2���2sh� Y�^H+��\��4IMQ���B�s��C�@���HX�%�EB���|�ʋ�c�ΰRf+��8f�֫�V�i��<��z�Pay�r|��� "q���H�ἴ�g��Ҍ���)p����!�R��҈]�������i�nF����8��X����2��䶫����p)]�e���~����3���O�ukK�!b	T5>_ijku�{/��՗��@��|N�ۻS�&�K�`���*1����v����+!��Ɲ����>�eV���oW���t��Xo�O�܎�%1�+.�MV�I���䲌ۄ�(��+h%��⺦hU�ܢ@>����D�z9�1'�㺙zrg�׸��߁�
���7���R�ZI���-�޶'ePN�D.�3�\�G���i �Q؋��ȓɃ奙�S3��݀�����ӑ�Cd��M�~���A5�R�y�<�N�����$ВN�?�W��!�	F�7v+vMC^7��=�7O�>��YL��)���1�M��y��*_3���S?�'%���d���p>�K���V����R
��Ms�o���$�}Z6;����rY����ڏ�Ɔi��x<�Y[�k��T"�����*��s-�	}�<�S���}nD6�����Q�ڵo�����^6�(F��1��u�?���_μҋ�嗕J{�	=��T�����=���s��_"�	S�u_�'hẍ́�V�x/��d�G���䴆@,�[N�{3Ƃ�[I����<6��A1�M}J��e��^L����U⊖̘-#7��dls��"����㥰`l
*t�D��i�wx�5��F��țlw�ՆsؽvB�DFM��s�\�!0j�w���Ktc�q�{K�L����Yy ���g(!�Y�0T�Ψ�
ܘ�b�tQ�LKI��E>tJ)�m$����h<�$�k���MjW�K�����em/x3��3����5�ߜ��́W���c����\��u����e2����r0T�M:kQz����^�<��zv���I�9
��n��@��H'{�Y��Jj|#���୕���s���Ao�%��i�y�O�7+��'6
�is�'��~�t"�����Q	��?O�N����Y��\�P�R��r^)���0 H/����t_�%��MWshR����}~\h�?Ԥ�h۷�=� EG:D����jF,q�~��o�+����ڟ�q��ͬ�;[I�{{�R/���H���zj�ݶ#��jĀ�����M�6c��_c�_�_.Sk�ߗ�I�����f�K�!SR<T!��-t)q��zd׿��ƪ�g��g�M4?�V'W?����9�*|�oS霎C�;a��=|1�??ڈ�:���}c�[a�YH�4�Y�(L��+5��G���ë�١����P{�9�z��+t���?)�q��Y��~i^���.]�po�#<�6�K/��[����ĀNrCYq͎Ƹ(�a�Rzt��˧^�����>)3�Sj	Z�T�(A�l���)��Rb��x�G��)g��J�=��Z�!�1,�-pޓh&�����W� �HDN�&���J�eg�ε���Vd]0��tx����P)EkL4��p�E�9�h(��.m�`%��>�O���!� ��f�Ք��!�F��0�4�󕪱��2/.��XEj\v�Ka�c}jT��ʞ�tc�w( �� K� �����(8y��shR��ƞ�CK�s��5?&y�`	�
�3��s/��me�#��n�cD̅!�Nhnd����N�NA}��oI=�߼��D����₝8�7���P���ږr=N�}�p�K��Dw�GD��'���j����>l˾�S7���̪��R�O�L�|7e�ʭ��^�t� ܞ��ro0ҹ7�k�b���,?������lD�䐝頹��oz�Zj���Rs3I�=�ϝ�=�:�lzޏA���aU�)����˃3�Kyҏ�����3M3���	Xn��r}-��	�o��w� UR�ҡ(	lPk���r��|�[#(6CC@+����2�fj�#��u�X�Zb	^�C�E�%E�7(1b�miT��YCZ�8���K׍��;��n�@^&��Hfc�lL�������j�Opɷ@#���)i9�^XQ���{���п���e��6L#�־����I�,m�E*OW���)�W�_�b�gI%c�-�O��NoLX.��v��9��7j�y��G��6��nd�;�C��7Z����3�-�n��i�P����hś`߷���a�5�9���yჽ��B��#'�(u��������.�i��p�4�$#�m"$O�19%���t�\�Bu�'	��Ɗ��}��s�/���}J��Qk��5�mQ�}�_kL㎲�~���H�S����:/i��F|3i?���B�����9�.��.�]�I}�L�KMŷ�f���� ��yO�wՠ&gڲMs�����C�8^%��%
z��%�[���ӌ4�I���w��O1*���/}��5�!�q�F{y�����G�<�1�n�x?��4���g�؛}&ۀ��F�����;)��ڼ���ѣx|�W;���񾋪��E��T������͢�[�
��?d�ϭ�P�	�_7�l�f�����4�����9���&��q�q~b�3b젭�Zj͔��P���OJ�gw���%p�|F�/����K����i�������,���L��LK��kt���'���@r�b0�������O�-g-��;0��'?9r����Bp����nlUԿ7� ����ʋ�"�$H��$$x�9 N�QY(�&^�0%�n�`F$(��٩� ��e�/_�ǉI�SGH}����E�"r���`�������)��}_;����j~L�/��F�|גb���,�m1��+��ȱ�lp�R�t�ʎ� ��m����n���o3��oJ�ĵ�t�e��7�����E�x2�]�(JZ!��yj	��lN}cj��X.�=�tD^5
�����k�y^i�M%8�cWzX
��#v,i�}K�m^��0�߉��$B�l���2��a=��.�[�������>��s]Jm~܆F���G��K?�x�W�i�Ĥ\~N�g~�Ʉ�?��R��q[�|��4���UHMM��]W"�Ν��)J;��C��?j'�nW����M��z]/�e_���,u�V.��qT��2�w"T˔�����G��xzϥ�!l��A:krTG����c��n�=��ζ�F2)"���xA����Q��j �&���&p��}L����I1n�(a��N&9ܥ 06���c8X��s+��|���l��,xy�\o2$I	�K�ه�y�B�A�{Z��	��8�lV�����D\�5�\�r:������N�3M�dMu��"�#�7is��@��c�z�`%S��:m��B���;Z�!��K������u�|0�J���2�u��2��_��?���������������������������?0�����������o����ߞ����v���������ݟ����������_������?�����#�/������������[�������������������������?�������/������׿�����%      G   6   x�3��4�4202�50�52U04�2 !.SNS����������L*F���  I	�      I   L   x�E���0�s�/`S�Y�����<t`�C ӕ��LҴ�-z	^�OS`���{W輫y#ޯ�:��=��6���      K   ?   x�31�4202�54�50T04�20 ".3�����H����"lQm`6�21	��qqq ��      M   C   x�3�44��H�T(�LN-+-�W(�KT�+�/KT�OKK-*I�4202�50�50T00�#��=... [u�      N   |   x�m��1�Kn����QK��#�)J�bX"r���C-ճ1�[ٞ	���w������>+;H!��1�|oL56[;^��@,�Q\�:���<��v�G�,��y�;⣷���A��%"Oj&�      R   1  x�}�Mk�0��ʯ��#����[�`0�vَ��:Z+�;	��s������E�#�R	�6X[��;�'ܒFV��^�⪂���YdK��{:�`'U�2�j&���P�r9��uԈ2� ���s�WJ��
���lZ)AFE�*��5��3to|���j@�G��cNl��Nd�4�C�).�������G8�R��B��S{2�BH���o�em�h��N�[c�$��1���|�N�?sG�:������L�97�r�6��w��}���ȾxT�՜�q3�&����_�b+=�f�b�s�FҴ�]�e�7!�.     