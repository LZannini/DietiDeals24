PGDMP         .                |           dietideals24    14.4    14.4 H    <           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            =           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            >           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            ?           1262    58738    dietideals24    DATABASE     h   CREATE DATABASE dietideals24 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE dietideals24;
                postgres    false            G           1247    58740    tipo_utente    TYPE     ^   CREATE TYPE public.tipo_utente AS ENUM (
    'COMPRATORE',
    'VENDITORE',
    'COMPLETO'
);
    DROP TYPE public.tipo_utente;
       public          postgres    false            �            1255    58844    check_offerta_creatore()    FUNCTION     �  CREATE FUNCTION public.check_offerta_creatore() RETURNS trigger
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
       public          postgres    false            �            1255    58747    check_offerta_presente()    FUNCTION     U  CREATE FUNCTION public.check_offerta_presente() RETURNS trigger
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
       public          postgres    false            �            1259    58748    asta    TABLE     �   CREATE TABLE public.asta (
    id integer NOT NULL,
    id_creatore integer NOT NULL,
    nome character varying(32) NOT NULL,
    descrizione character varying(256),
    categoria smallint NOT NULL,
    foto bytea
);
    DROP TABLE public.asta;
       public         heap    postgres    false            �            1259    58753    asta_id_creatore_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_creatore_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.asta_id_creatore_seq;
       public          postgres    false    209            @           0    0    asta_id_creatore_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.asta_id_creatore_seq OWNED BY public.asta.id_creatore;
          public          postgres    false    210            �            1259    58754    asta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.asta_id_seq;
       public          postgres    false    209            A           0    0    asta_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.asta_id_seq OWNED BY public.asta.id;
          public          postgres    false    211            �            1259    58755    asta_inversa    TABLE     �   CREATE TABLE public.asta_inversa (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    scadenza character varying(255) NOT NULL
);
     DROP TABLE public.asta_inversa;
       public         heap    postgres    false            �            1259    58758    asta_inversa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_inversa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_inversa_id_asta_seq;
       public          postgres    false    212            B           0    0    asta_inversa_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_inversa_id_asta_seq OWNED BY public.asta_inversa.id_asta;
          public          postgres    false    213            �            1259    58759    asta_ribasso    TABLE     �   CREATE TABLE public.asta_ribasso (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    timer character varying(255) NOT NULL,
    decremento double precision NOT NULL,
    minimo double precision NOT NULL
);
     DROP TABLE public.asta_ribasso;
       public         heap    postgres    false            �            1259    58762    asta_ribasso_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_ribasso_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_ribasso_id_asta_seq;
       public          postgres    false    214            C           0    0    asta_ribasso_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_ribasso_id_asta_seq OWNED BY public.asta_ribasso.id_asta;
          public          postgres    false    215            �            1259    58763    asta_silenziosa    TABLE     t   CREATE TABLE public.asta_silenziosa (
    id_asta integer NOT NULL,
    scadenza character varying(255) NOT NULL
);
 #   DROP TABLE public.asta_silenziosa;
       public         heap    postgres    false            �            1259    58766    asta_silenziosa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_silenziosa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.asta_silenziosa_id_asta_seq;
       public          postgres    false    216            D           0    0    asta_silenziosa_id_asta_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.asta_silenziosa_id_asta_seq OWNED BY public.asta_silenziosa.id_asta;
          public          postgres    false    217            �            1259    58767    notifica    TABLE     �   CREATE TABLE public.notifica (
    id integer NOT NULL,
    id_utente integer NOT NULL,
    testo character varying(128) NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    letta boolean NOT NULL
);
    DROP TABLE public.notifica;
       public         heap    postgres    false            �            1259    58770    offerta    TABLE     �   CREATE TABLE public.offerta (
    id_utente integer NOT NULL,
    id_asta integer NOT NULL,
    valore double precision NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    id integer NOT NULL
);
    DROP TABLE public.offerta;
       public         heap    postgres    false            �            1259    58773    offerta_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.offerta_id_asta_seq;
       public          postgres    false    219            E           0    0    offerta_id_asta_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.offerta_id_asta_seq OWNED BY public.offerta.id_asta;
          public          postgres    false    220            �            1259    58774    offerta_id_seq    SEQUENCE        CREATE SEQUENCE public.offerta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;
 %   DROP SEQUENCE public.offerta_id_seq;
       public          postgres    false    219            F           0    0    offerta_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.offerta_id_seq OWNED BY public.offerta.id;
          public          postgres    false    221            �            1259    58775    offerta_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.offerta_id_utente_seq;
       public          postgres    false    219            G           0    0    offerta_id_utente_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.offerta_id_utente_seq OWNED BY public.offerta.id_utente;
          public          postgres    false    222            �            1259    58776    utente    TABLE     T  CREATE TABLE public.utente (
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
       public         heap    postgres    false            �            1259    58781    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public          postgres    false    223            H           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public          postgres    false    224            �           2604    58782    asta id    DEFAULT     b   ALTER TABLE ONLY public.asta ALTER COLUMN id SET DEFAULT nextval('public.asta_id_seq'::regclass);
 6   ALTER TABLE public.asta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    211    209            �           2604    58783    asta id_creatore    DEFAULT     t   ALTER TABLE ONLY public.asta ALTER COLUMN id_creatore SET DEFAULT nextval('public.asta_id_creatore_seq'::regclass);
 ?   ALTER TABLE public.asta ALTER COLUMN id_creatore DROP DEFAULT;
       public          postgres    false    210    209            �           2604    58784    asta_inversa id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_inversa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_inversa_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_inversa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    213    212            �           2604    58785    asta_ribasso id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_ribasso ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_ribasso_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_ribasso ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    215    214            �           2604    58786    asta_silenziosa id_asta    DEFAULT     �   ALTER TABLE ONLY public.asta_silenziosa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_silenziosa_id_asta_seq'::regclass);
 F   ALTER TABLE public.asta_silenziosa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    217    216            �           2604    58787    offerta id_utente    DEFAULT     v   ALTER TABLE ONLY public.offerta ALTER COLUMN id_utente SET DEFAULT nextval('public.offerta_id_utente_seq'::regclass);
 @   ALTER TABLE public.offerta ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    222    219            �           2604    58788    offerta id_asta    DEFAULT     r   ALTER TABLE ONLY public.offerta ALTER COLUMN id_asta SET DEFAULT nextval('public.offerta_id_asta_seq'::regclass);
 >   ALTER TABLE public.offerta ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    220    219            �           2604    58789 
   offerta id    DEFAULT     h   ALTER TABLE ONLY public.offerta ALTER COLUMN id SET DEFAULT nextval('public.offerta_id_seq'::regclass);
 9   ALTER TABLE public.offerta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    219            �           2604    58790 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    224    223            *          0    58748    asta 
   TABLE DATA           S   COPY public.asta (id, id_creatore, nome, descrizione, categoria, foto) FROM stdin;
    public          postgres    false    209   xT       -          0    58755    asta_inversa 
   TABLE DATA           A   COPY public.asta_inversa (id_asta, prezzo, scadenza) FROM stdin;
    public          postgres    false    212   U       /          0    58759    asta_ribasso 
   TABLE DATA           R   COPY public.asta_ribasso (id_asta, prezzo, timer, decremento, minimo) FROM stdin;
    public          postgres    false    214   PU       1          0    58763    asta_silenziosa 
   TABLE DATA           <   COPY public.asta_silenziosa (id_asta, scadenza) FROM stdin;
    public          postgres    false    216   mU       3          0    58767    notifica 
   TABLE DATA           E   COPY public.notifica (id, id_utente, testo, data, letta) FROM stdin;
    public          postgres    false    218   �U       4          0    58770    offerta 
   TABLE DATA           G   COPY public.offerta (id_utente, id_asta, valore, data, id) FROM stdin;
    public          postgres    false    219   �U       8          0    58776    utente 
   TABLE DATA           h   COPY public.utente (id, username, email, password, biografia, sitoweb, paese, avatar, tipo) FROM stdin;
    public          postgres    false    223   V       I           0    0    asta_id_creatore_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.asta_id_creatore_seq', 1, false);
          public          postgres    false    210            J           0    0    asta_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.asta_id_seq', 47, true);
          public          postgres    false    211            K           0    0    asta_inversa_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_inversa_id_asta_seq', 1, false);
          public          postgres    false    213            L           0    0    asta_ribasso_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_ribasso_id_asta_seq', 1, false);
          public          postgres    false    215            M           0    0    asta_silenziosa_id_asta_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.asta_silenziosa_id_asta_seq', 1, false);
          public          postgres    false    217            N           0    0    offerta_id_asta_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.offerta_id_asta_seq', 1, false);
          public          postgres    false    220            O           0    0    offerta_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.offerta_id_seq', 4, true);
          public          postgres    false    221            P           0    0    offerta_id_utente_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.offerta_id_utente_seq', 1, false);
          public          postgres    false    222            Q           0    0    utente_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.utente_id_seq', 13, true);
          public          postgres    false    224            �           2606    58792    asta asta_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_pkey;
       public            postgres    false    209            �           2606    58794    notifica notifica_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_pkey;
       public            postgres    false    218            �           2606    58796    offerta offerta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_pkey;
       public            postgres    false    219            �           2606    58849    utente unique_email 
   CONSTRAINT     O   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT unique_email UNIQUE (email);
 =   ALTER TABLE ONLY public.utente DROP CONSTRAINT unique_email;
       public            postgres    false    223            �           2606    58847    utente unique_username 
   CONSTRAINT     U   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT unique_username UNIQUE (username);
 @   ALTER TABLE ONLY public.utente DROP CONSTRAINT unique_username;
       public            postgres    false    223            �           2606    58798    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    223            �           2620    58799    offerta blocca_offerte_doppie    TRIGGER     �   CREATE TRIGGER blocca_offerte_doppie BEFORE INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.check_offerta_presente();
 6   DROP TRIGGER blocca_offerte_doppie ON public.offerta;
       public          postgres    false    225    219            �           2620    58845 &   offerta check_offerta_creatore_trigger    TRIGGER     �   CREATE TRIGGER check_offerta_creatore_trigger BEFORE INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.check_offerta_creatore();
 ?   DROP TRIGGER check_offerta_creatore_trigger ON public.offerta;
       public          postgres    false    219    226            �           2606    58800    asta asta_id_creatore_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_id_creatore_fkey FOREIGN KEY (id_creatore) REFERENCES public.utente(id);
 D   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_id_creatore_fkey;
       public          postgres    false    223    3221    209            �           2606    58805 &   asta_inversa asta_inversa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_inversa
    ADD CONSTRAINT asta_inversa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_inversa DROP CONSTRAINT asta_inversa_id_asta_fkey;
       public          postgres    false    3211    212    209            �           2606    58810 &   asta_ribasso asta_ribasso_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_ribasso
    ADD CONSTRAINT asta_ribasso_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_ribasso DROP CONSTRAINT asta_ribasso_id_asta_fkey;
       public          postgres    false    209    3211    214            �           2606    58815 ,   asta_silenziosa asta_silenziosa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_silenziosa
    ADD CONSTRAINT asta_silenziosa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 V   ALTER TABLE ONLY public.asta_silenziosa DROP CONSTRAINT asta_silenziosa_id_asta_fkey;
       public          postgres    false    216    3211    209            �           2606    58820     notifica notifica_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 J   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_id_utente_fkey;
       public          postgres    false    223    3221    218            �           2606    58825    offerta offerta_id_asta_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 F   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_asta_fkey;
       public          postgres    false    219    3211    209            �           2606    58830    offerta offerta_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 H   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_utente_fkey;
       public          postgres    false    219    223    3221            *   �   x�-�1�0F�미]cliѕ�8HHXXj�@�#�^LX���g-dP�!M�����;c�4\(%�1AM���W'Lx��z�1?��<�GV-_��\6��ȡ��2�%y��QB�� ��qP0�����G�N)�"�,�      -   (   x�31�450�4202�50�54S04�24�20������ \sj      /      x������ � �      1   9   x�31�4202�50�54S04�24�20�21�.l�i��J�(ZXp��qqq ̲�      3      x������ � �      4   5   x�3�41"N##]S]#SCS+c+��!�����vyc�=... �dT      8   �   x�}��j�0���S�	B��	�v�i�B�l���VH�����t0ׇ��~d}�/����Vvp[�\�D�UF<�q������Uc�/��CU��8��b	�T7��D���P���NSzʉZ�G�a6̦�>d�$A~�@[x�]>�|�qU����m�8��0�� �#6���b1��C�Õ���
��֞�f�L�/-'R��B��o[���=��m�ݦ��9H:�O�(;��9F��+����
     