--创建数据库,数据库名：memodb

-- ----------------------------
-- Table structure for songs  这里插入10首周杰伦的歌作为歌单
-- ----------------------------
DROP TABLE IF EXISTS "public"."songs";
CREATE TABLE "public"."songs" (
                                  "id" bigserial NOT NULL,
                                  "artist" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                  "cover_url" varchar(1000) COLLATE "pg_catalog"."default",
                                  "created_at" timestamp(6) NOT NULL,
                                  "duration" int4,
                                  "music_url" varchar(1000) COLLATE "pg_catalog"."default",
                                  "song_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                  "title" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of songs
-- ----------------------------
INSERT INTO "public"."songs" VALUES (5, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s4s11/89/774616642.jpg', '2025-10-01 15:09:36.780191', 240, 'https://er-sycdn.kuwo.cn/1fee4caf337c3413a90880011a0bdf4b/68dcdac6/resource/30106/trackmedia/M800001zMQr71F1Qo8.mp3', '5', '夜曲');
INSERT INTO "public"."songs" VALUES (6, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/64/39/3540704654.jpg', '2025-10-01 15:09:36.785621', 240, 'https://lw-sycdn.kuwo.cn/7fd44130937835be9576c6d6d0f1813f/68dcdaeb/resource/30106/trackmedia/M800002QE4Dt4Gkrgd.mp3', '6', '告白气球');
INSERT INTO "public"."songs" VALUES (7, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s4s36/70/1529234453.jpg', '2025-10-01 15:09:36.792586', 240, 'https://er-sycdn.kuwo.cn/940e7b7ed839ba9e0673534394b0522f/68dcdb0b/resource/30106/trackmedia/M8000009BCJK1nRaad.mp3', '7', '简单爱');
INSERT INTO "public"."songs" VALUES (8, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s4s11/89/774616642.jpg', '2025-10-01 15:09:36.798965', 240, 'https://er-sycdn.kuwo.cn/d9eee8340b3cec292a7133a65e117ec3/68dcdb33/resource/30106/trackmedia/M800003KtYhg4frNXC.mp3', '8', '枫');
INSERT INTO "public"."songs" VALUES (9, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/7/83/4087363627.jpg', '2025-10-01 15:09:36.803858', 240, 'https://er-sycdn.kuwo.cn/a96001d6bcf5995b44640d057fe8b333/68dcdb55/resource/30106/trackmedia/M800002qU5aY3Qu24y.mp3', '9', '青花瓷');
INSERT INTO "public"."songs" VALUES (10, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s3s94/93/211513640.jpg', '2025-10-01 15:09:36.810015', 240, 'https://er-sycdn.kuwo.cn/90115ecc374c8cf34f82428d905e6c78/68dcdb78/resource/30106/trackmedia/M800003uEbEr0jcW7c.mp3', '10', '东风破');
INSERT INTO "public"."songs" VALUES (2, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/12/37/4156270827.jpg', '2025-10-01 15:09:36.764217', 240, 'https://er-sycdn.kuwo.cn/2e186b213907255b7ce2533c923ffa92/68dcd83a/resource/30106/trackmedia/M800000SOnCR1nyjIV.mp3', '2', '说好不哭');
INSERT INTO "public"."songs" VALUES (1, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s3s94/93/211513640.jpg', '2025-10-01 15:09:36.697712', 240, 'https://er-sycdn.kuwo.cn/29920c43703f5461c3f3b4df6ab0ff32/68dcd7ec/resource/30106/trackmedia/M8000039MnYb0qxYhV.mp3', '1', '晴天');
INSERT INTO "public"."songs" VALUES (3, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s4s0/93/1794217775.jpg', '2025-10-01 15:09:36.769514', 240, 'https://er-sycdn.kuwo.cn/54e2b7cf36106371f7651c083129be6f/68dcd8a8/resource/30106/trackmedia/M800003aAYrm3GE0Ac.mp3', '3', '稻香');
INSERT INTO "public"."songs" VALUES (4, '周杰伦', 'https://img1.kuwo.cn/star/albumcover/{x}/s4s81/2/3200337129.jpg', '2025-10-01 15:09:36.774415', 240, 'https://lv-sycdn.kuwo.cn/d000a6d6298a4615d9ded056e0ce308a/68dcda9d/resource/30106/trackmedia/M800004Z8Ihr0JIu5s.mp3', '4', '七里香');

-- ----------------------------
-- Uniques structure for table songs
-- ----------------------------
ALTER TABLE "public"."songs" ADD CONSTRAINT "uk_ku26ynojiscscm1fty26l9gr3" UNIQUE ("song_id");

-- ----------------------------
-- Primary Key structure for table songs
-- ----------------------------
ALTER TABLE "public"."songs" ADD CONSTRAINT "songs_pkey" PRIMARY KEY ("id");
