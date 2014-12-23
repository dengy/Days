package com.android.internal.widget.chinesecalendar.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;
import com.inde.shiningdays.R;

public class CalendarUtils {

	// public static String[] nStr1 = { "��", "һ", "��", "��", "��", "��", "��", "��",
	// "��", "��", "ʮ" };
	public static String[] nStr2 = null;
	public static int[] nStr2Id = { R.string.zzzzz_chu, R.string.zzzzz_ten,
			R.string.zzzzz_twenty, R.string.zzzzz_thirty };

	public static String[] zrxName1;
	// 十二神，即青龙，明堂，天刑，朱雀，金匮，天德，白虎，玉堂，天牢，玄武，司命，勾陈
	public static int[] zrxName1Id ={R.string.zzzzz_zrx1,R.string.zzzzz_zrx2,R.string.zzzzz_zrx3,R.string.zzzzz_zrx4,R.string.zzzzz_zrx5,
		R.string.zzzzz_zrx6,R.string.zzzzz_zrx7,R.string.zzzzz_zrx8,R.string.zzzzz_zrx9,R.string.zzzzz_zrx10,R.string.zzzzz_zrx11,R.string.zzzzz_zrx12,};
	public static String[] zrxName2 ;
	public static int[] zrxName2Id ={R.string.zzzzz_zrx11,R.string.zzzzz_zrx12,R.string.zzzzz_zrx1,R.string.zzzzz_zrx2,R.string.zzzzz_zrx3,
		R.string.zzzzz_zrx4,R.string.zzzzz_zrx5,R.string.zzzzz_zrx6,R.string.zzzzz_zrx7,R.string.zzzzz_zrx8,R.string.zzzzz_zrx9,R.string.zzzzz_zrx10};
	public static String[] zrxName3 ;
	public static int[] zrxName3Id ={R.string.zzzzz_zrx9,R.string.zzzzz_zrx10,R.string.zzzzz_zrx11,R.string.zzzzz_zrx12,R.string.zzzzz_zrx1,
		R.string.zzzzz_zrx2,R.string.zzzzz_zrx3,R.string.zzzzz_zrx4,R.string.zzzzz_zrx5,R.string.zzzzz_zrx6,R.string.zzzzz_zrx7,R.string.zzzzz_zrx8};
	public static String[] zrxName4 ;
	public static int[] zrxName4Id ={R.string.zzzzz_zrx7,R.string.zzzzz_zrx8,R.string.zzzzz_zrx9,R.string.zzzzz_zrx10,R.string.zzzzz_zrx11,
		R.string.zzzzz_zrx12,R.string.zzzzz_zrx1,R.string.zzzzz_zrx2,R.string.zzzzz_zrx3,R.string.zzzzz_zrx4,R.string.zzzzz_zrx5,R.string.zzzzz_zrx6};
	public static String[] zrxName5 ;
	public static int[] zrxName5Id ={R.string.zzzzz_zrx5,R.string.zzzzz_zrx6,R.string.zzzzz_zrx7,R.string.zzzzz_zrx8,R.string.zzzzz_zrx9,
		R.string.zzzzz_zrx10,R.string.zzzzz_zrx11,R.string.zzzzz_zrx12,R.string.zzzzz_zrx1,R.string.zzzzz_zrx2,R.string.zzzzz_zrx3,R.string.zzzzz_zrx4};
	public static String[] zrxName6 ;
	public static int[] zrxName6Id ={R.string.zzzzz_zrx3,R.string.zzzzz_zrx4,R.string.zzzzz_zrx5,R.string.zzzzz_zrx6,R.string.zzzzz_zrx7,
		R.string.zzzzz_zrx8,R.string.zzzzz_zrx9,R.string.zzzzz_zrx10,R.string.zzzzz_zrx11,R.string.zzzzz_zrx12,R.string.zzzzz_zrx1,R.string.zzzzz_zrx2};
	public static String[] zrxName7 ;
	public static int[] zrxName7Id =zrxName1Id;
	public static String[] zrxName8 ;
	public static int[] zrxName8Id =zrxName2Id;
	public static String[] zrxName9 ;
	public static int[] zrxName9Id =zrxName3Id;
	public static String[] zrxName10 ;
	public static int[] zrxName10Id =zrxName4Id;
	public static String[] zrxName11 ;
	public static int[] zrxName11Id =zrxName5Id;
	public static String[] zrxName12 ;
	public static int[] zrxName12Id =zrxName6Id;

	public static String[] wuxing = null;
	public static int[] wuxingId={R.string.zzzzz_wu1,R.string.zzzzz_wu2,R.string.zzzzz_wu3,R.string.zzzzz_wu4,R.string.zzzzz_wu5,
		R.string.zzzzz_wu6,R.string.zzzzz_wu7,R.string.zzzzz_wu8,R.string.zzzzz_wu9,R.string.zzzzz_wu10,
		R.string.zzzzz_wu11,R.string.zzzzz_wu12,R.string.zzzzz_wu13,R.string.zzzzz_wu14,R.string.zzzzz_wu15,
		R.string.zzzzz_wu16,R.string.zzzzz_wu17,R.string.zzzzz_wu18,R.string.zzzzz_wu19,R.string.zzzzz_wu20,
		R.string.zzzzz_wu21,R.string.zzzzz_wu22,R.string.zzzzz_wu23,R.string.zzzzz_wu24,R.string.zzzzz_wu25
	};
	
	public static String[] sFtv = null;

	public static int[] sFtvId = { R.string.zzzzz_sFestival1, R.string.zzzzz_sFestival2,
			R.string.zzzzz_sFestival3, R.string.zzzzz_sFestival4, R.string.zzzzz_sFestival5,
			R.string.zzzzz_sFestival6, R.string.zzzzz_sFestival7, R.string.zzzzz_sFestival8,
			R.string.zzzzz_sFestival9, R.string.zzzzz_sFestival10, R.string.zzzzz_sFestival11,
			R.string.zzzzz_sFestival12, R.string.zzzzz_sFestival13, R.string.zzzzz_sFestival14,
			R.string.zzzzz_sFestival15, R.string.zzzzz_sFestival16, R.string.zzzzz_sFestival17,
			R.string.zzzzz_sFestival18, R.string.zzzzz_sFestival19, R.string.zzzzz_sFestival20,
			R.string.zzzzz_sFestival21, R.string.zzzzz_sFestival22, R.string.zzzzz_sFestival23,
			R.string.zzzzz_sFestival24 };

	public static String[] lFtv = null;
	public static int[] lFtvId = { R.string.zzzzz_lFestival1, R.string.zzzzz_lFestival2,
			R.string.zzzzz_lFestival3, R.string.zzzzz_lFestival4, R.string.zzzzz_lFestival5,
			R.string.zzzzz_lFestival6, R.string.zzzzz_lFestival7, R.string.zzzzz_lFestival8,
			R.string.zzzzz_lFestival9, R.string.zzzzz_lFestival10, R.string.zzzzz_lFestival11,
			R.string.zzzzz_lFestival12, R.string.zzzzz_lFestival13, R.string.zzzzz_lFestival14,
			R.string.zzzzz_lFestival15, R.string.zzzzz_lFestival16, R.string.zzzzz_lFestival17,
			R.string.zzzzz_lFestival18, R.string.zzzzz_lFestival19, R.string.zzzzz_lFestival20,
			R.string.zzzzz_lFestival21, R.string.zzzzz_lFestival22, R.string.zzzzz_lFestival23,
			R.string.zzzzz_lFestival24, R.string.zzzzz_lFestival25, R.string.zzzzz_lFestival26,
			R.string.zzzzz_lFestival27, R.string.zzzzz_lFestival28, R.string.zzzzz_lFestival29,
			R.string.zzzzz_lFestival30 };

	public static String[] wFtv = null;

	static int[] wFtvId = new int[] { R.string.zzzzz_wFestival1, R.string.zzzzz_wFestival2,
			R.string.zzzzz_wFestival3, R.string.zzzzz_wFestival4, R.string.zzzzz_wFestival5,
			R.string.zzzzz_wFestival6, R.string.zzzzz_wFestival7, R.string.zzzzz_wFestival8,
			R.string.zzzzz_wFestival9, R.string.zzzzz_wFestival10 };

	public static String[] solarTerm = null;

	public static int[] sTermId = { R.string.zzzzz_sTerm1, R.string.zzzzz_sTerm2,
			R.string.zzzzz_sTerm3, R.string.zzzzz_sTerm4, R.string.zzzzz_sTerm5, R.string.zzzzz_sTerm6,
			R.string.zzzzz_sTerm7, R.string.zzzzz_sTerm8, R.string.zzzzz_sTerm9,
			R.string.zzzzz_sTerm10, R.string.zzzzz_sTerm11, R.string.zzzzz_sTerm12,
			R.string.zzzzz_sTerm13, R.string.zzzzz_sTerm14, R.string.zzzzz_sTerm15,
			R.string.zzzzz_sTerm16, R.string.zzzzz_sTerm17, R.string.zzzzz_sTerm18,
			R.string.zzzzz_sTerm19, R.string.zzzzz_sTerm20, R.string.zzzzz_sTerm21,
			R.string.zzzzz_sTerm22, R.string.zzzzz_sTerm23, R.string.zzzzz_sTerm24 };

	// public static String[] dayglk = { "��", "î", "��", "��", "��", "��", "��", "��",
	// "��", "��" };
	public static String[] jcName0 = null;
	static int[] jcName0Id = new int[] { R.string.zzzzz_jc1, R.string.zzzzz_jc2,
			R.string.zzzzz_jc3, R.string.zzzzz_jc4, R.string.zzzzz_jc5, R.string.zzzzz_jc6,
			R.string.zzzzz_jc7, R.string.zzzzz_jc8, R.string.zzzzz_jc9, R.string.zzzzz_jc10,
			R.string.zzzzz_jc11, R.string.zzzzz_jc12 };

	public static String[] jcName1 = null;

	static int[] jcName1Id = new int[] { R.string.zzzzz_jc12, R.string.zzzzz_jc1,
			R.string.zzzzz_jc2, R.string.zzzzz_jc3, R.string.zzzzz_jc4, R.string.zzzzz_jc5,
			R.string.zzzzz_jc6, R.string.zzzzz_jc7, R.string.zzzzz_jc8, R.string.zzzzz_jc9,
			R.string.zzzzz_jc10, R.string.zzzzz_jc11 };

	public static String[] jcName2 = null;

	static int[] jcName2Id = new int[] { R.string.zzzzz_jc11, R.string.zzzzz_jc12,
			R.string.zzzzz_jc1, R.string.zzzzz_jc2, R.string.zzzzz_jc3, R.string.zzzzz_jc4,
			R.string.zzzzz_jc5, R.string.zzzzz_jc6, R.string.zzzzz_jc7, R.string.zzzzz_jc8,
			R.string.zzzzz_jc9, R.string.zzzzz_jc10 };

	public static String[] jcName3 = null;

	static int[] jcName3Id = new int[] { R.string.zzzzz_jc10, R.string.zzzzz_jc11,
			R.string.zzzzz_jc12, R.string.zzzzz_jc1, R.string.zzzzz_jc2, R.string.zzzzz_jc3,
			R.string.zzzzz_jc4, R.string.zzzzz_jc5, R.string.zzzzz_jc6, R.string.zzzzz_jc7,
			R.string.zzzzz_jc8, R.string.zzzzz_jc9 };

	public static String[] jcName4 = null;

	static int[] jcName4Id = new int[] { R.string.zzzzz_jc9, R.string.zzzzz_jc10,
			R.string.zzzzz_jc11, R.string.zzzzz_jc12, R.string.zzzzz_jc1, R.string.zzzzz_jc2,
			R.string.zzzzz_jc3, R.string.zzzzz_jc4, R.string.zzzzz_jc5, R.string.zzzzz_jc6,
			R.string.zzzzz_jc7, R.string.zzzzz_jc8 };
	public static String[] jcName5 = null;
	static int[] jcName5Id = new int[] { R.string.zzzzz_jc8, R.string.zzzzz_jc9,
			R.string.zzzzz_jc10, R.string.zzzzz_jc11, R.string.zzzzz_jc12, R.string.zzzzz_jc1,
			R.string.zzzzz_jc2, R.string.zzzzz_jc3, R.string.zzzzz_jc4, R.string.zzzzz_jc5,
			R.string.zzzzz_jc6, R.string.zzzzz_jc7 };
	public static String[] jcName6 = null;

	static int[] jcName6Id = new int[] { R.string.zzzzz_jc7, R.string.zzzzz_jc8,
			R.string.zzzzz_jc9, R.string.zzzzz_jc10, R.string.zzzzz_jc11, R.string.zzzzz_jc12,
			R.string.zzzzz_jc1, R.string.zzzzz_jc2, R.string.zzzzz_jc3, R.string.zzzzz_jc4,
			R.string.zzzzz_jc5, R.string.zzzzz_jc6 };
	public static String[] jcName7 = null;

	static int[] jcName7Id = new int[] { R.string.zzzzz_jc6, R.string.zzzzz_jc7,
			R.string.zzzzz_jc8, R.string.zzzzz_jc9, R.string.zzzzz_jc10, R.string.zzzzz_jc11,
			R.string.zzzzz_jc12, R.string.zzzzz_jc1, R.string.zzzzz_jc2, R.string.zzzzz_jc3,
			R.string.zzzzz_jc4, R.string.zzzzz_jc5 };

	public static String[] jcName8 = null;
	static int[] jcName8Id = new int[] { R.string.zzzzz_jc5, R.string.zzzzz_jc6,
			R.string.zzzzz_jc7, R.string.zzzzz_jc8, R.string.zzzzz_jc9, R.string.zzzzz_jc10,
			R.string.zzzzz_jc11, R.string.zzzzz_jc12, R.string.zzzzz_jc1, R.string.zzzzz_jc2,
			R.string.zzzzz_jc3, R.string.zzzzz_jc4 };
	public static String[] jcName9 = null;
	static int[] jcName9Id = new int[] { R.string.zzzzz_jc4, R.string.zzzzz_jc5,
			R.string.zzzzz_jc6, R.string.zzzzz_jc7, R.string.zzzzz_jc8, R.string.zzzzz_jc9,
			R.string.zzzzz_jc10, R.string.zzzzz_jc11, R.string.zzzzz_jc12, R.string.zzzzz_jc1,
			R.string.zzzzz_jc2, R.string.zzzzz_jc3 };
	public static String[] jcName10 = null;
	static int[] jcName10Id = new int[] { R.string.zzzzz_jc3, R.string.zzzzz_jc4,
			R.string.zzzzz_jc5, R.string.zzzzz_jc6, R.string.zzzzz_jc7, R.string.zzzzz_jc8,
			R.string.zzzzz_jc9, R.string.zzzzz_jc10, R.string.zzzzz_jc11, R.string.zzzzz_jc12,
			R.string.zzzzz_jc1, R.string.zzzzz_jc2 };
	public static String[] jcName11 = null;
	static int[] jcName11Id = new int[] { R.string.zzzzz_jc2, R.string.zzzzz_jc3,
			R.string.zzzzz_jc4, R.string.zzzzz_jc5, R.string.zzzzz_jc6, R.string.zzzzz_jc7,
			R.string.zzzzz_jc8, R.string.zzzzz_jc9, R.string.zzzzz_jc10, R.string.zzzzz_jc11,
			R.string.zzzzz_jc12, R.string.zzzzz_jc1 };
	public static int[] solarMonth = new int[] { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 }; // ÿ���µ�����
	public static String[] GAN = null;

	static int[] GanId = new int[] { R.string.zzzzz_gan01, R.string.zzzzz_gan02,
			R.string.zzzzz_gan03, R.string.zzzzz_gan04, R.string.zzzzz_gan05, R.string.zzzzz_gan06,
			R.string.zzzzz_gan07, R.string.zzzzz_gan08, R.string.zzzzz_gan09, R.string.zzzzz_gan010 };

	public static String[] ZHI = null;

	static int[] Zhi0Id = new int[] { R.string.zzzzz_zhi01, R.string.zzzzz_zhi02,
			R.string.zzzzz_zhi03, R.string.zzzzz_zhi04, R.string.zzzzz_zhi05, R.string.zzzzz_zhi06,
			R.string.zzzzz_zhi07, R.string.zzzzz_zhi08, R.string.zzzzz_zhi09, R.string.zzzzz_zhi010,
			R.string.zzzzz_zhi011, R.string.zzzzz_zhi012 };
	public static int[] sTermInfo = { 0, 21208, 42467, 63836, 85337, 107014,
			128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989,
			308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224,
			483532, 504758 };
	static String[] Gan5 = null;

	static int[] Gan5Id = new int[] { R.string.zzzzz_gan05, R.string.zzzzz_gan06,
			R.string.zzzzz_gan07, R.string.zzzzz_gan08, R.string.zzzzz_gan09, R.string.zzzzz_gan010,
			R.string.zzzzz_gan01, R.string.zzzzz_gan02, R.string.zzzzz_gan03, R.string.zzzzz_gan04 };

	static String[] sfw = null;

	static int[] sfwId = new int[] { R.string.zzzzz_south, R.string.zzzzz_easte,
			R.string.zzzzz_north, R.string.zzzzz_west, R.string.zzzzz_south, R.string.zzzzz_easte,
			R.string.zzzzz_north, R.string.zzzzz_west, R.string.zzzzz_south, R.string.zzzzz_easte,
			R.string.zzzzz_north, R.string.zzzzz_west };

	static String[] Zhi = null;

	static String[] Zhi3 = null;

	static int[] Zhi3Id = new int[] { R.string.zzzzz_zhi07, R.string.zzzzz_zhi08,
			R.string.zzzzz_zhi09, R.string.zzzzz_zhi010, R.string.zzzzz_zhi011, R.string.zzzzz_zhi012,
			R.string.zzzzz_zhi01, R.string.zzzzz_zhi02, R.string.zzzzz_zhi03, R.string.zzzzz_zhi04,
			R.string.zzzzz_zhi05, R.string.zzzzz_zhi06 };

	public static String[] AnimalIdx = null;
	static int[] AnimalId = new int[] { R.string.zzzzz_animal1, R.string.zzzzz_animal2,
			R.string.zzzzz_animal3, R.string.zzzzz_animal4, R.string.zzzzz_animal5,
			R.string.zzzzz_animal6, R.string.zzzzz_animal7, R.string.zzzzz_animal8,
			R.string.zzzzz_animal9, R.string.zzzzz_animal10, R.string.zzzzz_animal11,
			R.string.zzzzz_animal12 };
	static String[] AnimalIdx2 = null;

	static int[] AnimalId2 = new int[] { R.string.zzzzz_animal7, R.string.zzzzz_animal8,
			R.string.zzzzz_animal9, R.string.zzzzz_animal10, R.string.zzzzz_animal11,
			R.string.zzzzz_animal12, R.string.zzzzz_animal1, R.string.zzzzz_animal2,
			R.string.zzzzz_animal3, R.string.zzzzz_animal4, R.string.zzzzz_animal5,
			R.string.zzzzz_animal6 };
	// static String[] Gan3 = { "���� �ҳ� ���� ��î �쳽 ���� ���� ��δ ���� ���� ���� �Һ�",
	// "���� ���� ���� ��î �� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ���� ���� ��î �ɳ� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ���� ���� ��î �׳� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ��� ���� ��î �� ���� ���� ��δ ���� ���� ���� �ﺥ",
	// "���� �ҳ� ���� ��î �쳽 ���� ���� ��δ ���� ���� ���� �Һ�",
	// "���� ���� ���� ��î �� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ���� ���� ��î �ɳ� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ���� ���� ��î �׳� ���� ���� ��δ ���� ���� ���� ����",
	// "���� ��� ���� ��î �� ���� ���� ��δ ���� ���� ���� �ﺥ" };

	static String[] Gan3 = null;

	static int[] Gan3Id = new int[] { R.string.zzzzz_gan1, R.string.zzzzz_gan2,
			R.string.zzzzz_gan3, R.string.zzzzz_gan4, R.string.zzzzz_gan5, R.string.zzzzz_gan6,
			R.string.zzzzz_gan7, R.string.zzzzz_gan8, R.string.zzzzz_gan9, R.string.zzzzz_gan10 };
	// static String[] Gan2 = { "�ײ�����", "�Ҳ���ֲ", "������", "������ͷ", "�첻����", "������ȯ",
	// "����", "�����Ͻ�", "�ɲ���ˮ", "�ﲻ����" };
	static String[] Gan2 = null;

	static int[] Gan2Id = new int[] { R.string.zzzzz_gan21, R.string.zzzzz_gan22,
			R.string.zzzzz_gan23, R.string.zzzzz_gan24, R.string.zzzzz_gan25, R.string.zzzzz_gan26,
			R.string.zzzzz_gan27, R.string.zzzzz_gan28, R.string.zzzzz_gan29, R.string.zzzzz_gan210 };


	// "�粻ɻ��", "δ����ҩ", "�겻����", "�ϲ����", "�粻��Ȯ", "������Ȣ" };
	static String[] Zhi2 = null;
	static int[] ZhiId = new int[] { R.string.zzzzz_zhi1, R.string.zzzzz_zhi2,
			R.string.zzzzz_zhi3, R.string.zzzzz_zhi4, R.string.zzzzz_zhi5, R.string.zzzzz_zhi6,
			R.string.zzzzz_zhi7, R.string.zzzzz_zhi8, R.string.zzzzz_zhi9, R.string.zzzzz_zhi10,
			R.string.zzzzz_zhi11, R.string.zzzzz_zhi12 };
	
	// 星座信息
	static String[] mConstellations = null;
	static int[] mConstellationIds = new int[] { R.string.zzzzz_txt_cnstellation1, R.string.zzzzz_txt_cnstellation2, 
		R.string.zzzzz_txt_cnstellation3, R.string.zzzzz_txt_cnstellation4, R.string.zzzzz_txt_cnstellation5,
		R.string.zzzzz_txt_cnstellation6, R.string.zzzzz_txt_cnstellation7, R.string.zzzzz_txt_cnstellation8, 
		R.string.zzzzz_txt_cnstellation9, R.string.zzzzz_txt_cnstellation10, R.string.zzzzz_txt_cnstellation11,
		R.string.zzzzz_txt_cnstellation12 };

	public static int[] NongliData = new int[] { 19416, 19168, 42352, 21717,
			53856, 55632, 21844, 22191, 39632, 21970, 19168, 42422, 42192,
			53840, 53909, 46415, 54944, 44450, 38320, 18807, 18815, 42160,
			46261, 27216, 27968, 43860, 11119, 38256, 21234, 18800, 25958,
			54432, 59984, 27285, 23263, 11104, 34531, 37615, 51415, 51551,
			54432, 55462, 46431, 22176, 42420, 9695, 37584, 53938, 43344,
			46423, 27808, 46416, 21333, 19887, 42416, 17779, 21183, 43432,
			59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051,
			55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840,
			54616, 46400, 46752, 38310, 38335, 18864, 43380, 42160, 45690,
			27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859,
			59984, 27480, 23232, 43872, 38613, 37600, 51552, 55636, 54432,
			55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240,
			47780, 44368, 21977, 19360, 42416, 20854, 21183, 43312, 31060,
			27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576,
			23200, 30371, 38608, 19195, 19152, 42192, 53430, 53855, 54560,
			56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 45653,
			27951, 44448, 19299, 37759, 18936, 18800, 25776, 26790, 59999,
			27424, 42692, 43759, 37600, 53987, 51552, 54615, 54432, 55888,
			23893, 22176, 42704, 21972, 21200, 43448, 43344, 46240, 46758,
			44368, 21920, 43940, 42416, 21168, 45683, 26928, 29495, 27296,
			44368, 19285, 19311, 42352, 21732, 53856, 59752, 54560, 55968,
			27302, 22239, 19168, 43476, 42192, 53584, 62034, 54560 };

	/**
	 * 获取该月天数(阳历)
	 * @param y 阳历年
	 * @param m 阳历月
	 * @return 返回的是该月的天数(阳历)
	 */
	public static int solarDays(int y, int m) {
		if (m == 1)
			return (((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) ? 29
					: 28);
		else if (m > 11 || m < 0)
			return -1;
		else
			return (solarMonth[m]);
	}

	/**
	 * 根据年月日 返回该日在本周中的Index（周日是每个星期的第一天，周日~周六 对应着 1~7）
	 */
	public static int getDay(int y, int m, int d) {
		if (m < 0 || m > 11)
			return -1;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, y);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.DATE, d);
		return c.get(Calendar.DAY_OF_WEEK) - 1;

	}

	/**
	 * 返回天干地支
	 * 方法一： 十天干：  甲、乙、丙、丁、戊、己、庚、辛、壬、癸； 十二地支：子、丑、寅、卯、辰、巳、午、未、申、酉、戌、亥；
      *  天干地支纪年法首先是天干在前，
      *  地支在后，
	 * @param num 
	 * @return
	 */
	public static String cyclical(int num) {
		return (GAN[num % 10] + ZHI[num % 12]);
	}

	public static String cyclical4(int num) {
		return (Gan3[num % 10]);
	}

	/**
	 * 返回彭祖百忌 甲不开仓 子不问卜
	 * @param num
	 * @return
	 */
	public static String cyclical3(int num) {
		return (Gan2[num % 10] + " " + Zhi2[num % 12]);
	}

	/**
	 * 公历年月  返回农历的年月天干地支
	 * @param y
	 * @param m
	 * @return
	 */
	public static String getLunarYear(int y, int m) {
		String cY = "";
		if (m < 2) {
			cY = cyclical(y - 1900 + 36 - 1);
		} else {
			cY = cyclical(y - 1900 + 36);
		}
		return cY;
	}

	/**
	 * 返回十二生肖
	 * @param cy
	 * @return
	 */
	public static String getAnimal(String cy) {
		if (cy != null && cy.length() == 2) {
			String zhi = cy.substring(1);
			for (int i = 0; i < ZHI.length; i++) {
				if (ZHI[i].equals(zhi)) {
					return CalendarUtils.AnimalIdx[i];
				}
			}
		}
		return "";
	}

	/**
	 * 获取该节气日在所在公历月份的第几天
	 * @param y
	 * @param n
	 * @return
	 */
	public static int sTerm(int y, int n) {
		double millseconds = 31556925974.7 * (y - 1900) + (double) sTermInfo[n] * 60000;
		long mm = Date.UTC(0, 0, 6, 2, 5, 0);
		// p(y+","+n+":"+(millseconds+mm));
		Date date1 = new Date(((long) (millseconds + mm)));
		// p(date1.toString());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis((long) (millseconds + mm));
		DateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd");
		TimeZone gmtTime = TimeZone.getTimeZone("UTC");

		gmtFormat.setTimeZone(gmtTime);
		String s = gmtFormat.format(c.getTime());
		String dd = s.substring(8);

		return Integer.parseInt(dd);
	}

	static void p(String s) {
		System.out.println(s);
	}

	/*
	 * ��Ӧ��ũ�����ж����� y:������
	 */
	public static int lYearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			int n = NongliData[y - 1900];
			int m = n & i;
			if (m != 0) {
				sum += 1;
			}
		}
		return sum + leapDays(y);
	}

	/**
	 * 获取该年份闰月的天数，如无闰月则返回0
	 * @param y 农历年份
	 * @return
	 */
	public static int leapDays(int y) {
		int n = NongliData[y - 1899];
		if (leapMonth(y) != 0)
			return ((n & 0xf) == 0xf ? 30 : 29);
		else
			return (0);
	}

	/**
	 * 获取该年份闰月是全年第几个月
	 * @param y 农历年份
	 * @return 
	 */
	public static int leapMonth(int y) {
		int n = NongliData[y - 1900];
		int lm = n & 0xf;
		return (lm == 0xf ? 0 : lm);
	}

	/**
	 * 获取农历当月天数
	 * @param y 年
	 * @param m 月
	 * @return 农历当月天数
	 */
	public static int monthDays(int y, int m) {
		Log.i("yyy", "result 0====<><><><> year==" + y + "month===" + m);
		int nn = NongliData[y - 1900];
		Log.i("yyy", "result 1====");
		int result = (((nn & (0x10000 >> m)) != 0) ? 30 : 29);
		Log.i("yyy", "result 2====" + result + "\n");
		return result;
	}

	/**
	 * 获取五行信息
	 * @param dstr
	 * @return
	 */
	public static String jzny(String dstr) {
		String ny = "";
		Resources r = CalendarData.context.getResources();
		if (dstr.equals("00") || dstr.equals("11"))
			ny = r.getString(R.string.zzzzz_wu1);
		if (dstr.equals("22") || dstr.equals("33"))
			ny = r.getString(R.string.zzzzz_wu2);
		if (dstr.equals("44") || dstr.equals("55"))
			ny = r.getString(R.string.zzzzz_wu3);
		if (dstr.equals("66") || dstr.equals("77"))
			ny = r.getString(R.string.zzzzz_wu4);
		if (dstr.equals("88") || dstr.equals("99"))
			ny = r.getString(R.string.zzzzz_wu5);
		if (dstr.equals("010") || dstr.equals("111"))
			ny = r.getString(R.string.zzzzz_wu6);
		if (dstr.equals("20") || dstr.equals("31"))
			ny = r.getString(R.string.zzzzz_wu7);
		if (dstr.equals("42") || dstr.equals("53"))
			ny = r.getString(R.string.zzzzz_wu8);
		if (dstr.equals("64") || dstr.equals("75"))
			ny = r.getString(R.string.zzzzz_wu9);
		if (dstr.equals("86") || dstr.equals("97"))
			ny = r.getString(R.string.zzzzz_wu10);
		if (dstr.equals("08") || dstr.equals("19"))
			ny = r.getString(R.string.zzzzz_wu11);
		if (dstr.equals("210") || dstr.equals("311"))
			ny = r.getString(R.string.zzzzz_wu12);
		if (dstr.equals("40") || dstr.equals("51"))
			ny = r.getString(R.string.zzzzz_wu13);
		if (dstr.equals("62") || dstr.equals("73"))
			ny =r.getString(R.string.zzzzz_wu14);
		if (dstr.equals("84") || dstr.equals("95"))
			ny = r.getString(R.string.zzzzz_wu15);
		if (dstr.equals("06") || dstr.equals("17"))
			ny = r.getString(R.string.zzzzz_wu16);
		if (dstr.equals("28") || dstr.equals("39"))
			ny = r.getString(R.string.zzzzz_wu17);
		if (dstr.equals("410") || dstr.equals("511"))
			ny = r.getString(R.string.zzzzz_wu18);
		if (dstr.equals("60") || dstr.equals("71"))
			ny = r.getString(R.string.zzzzz_wu19);
		if (dstr.equals("82") || dstr.equals("93"))
			ny = r.getString(R.string.zzzzz_wu20);
		if (dstr.equals("04") || dstr.equals("15"))
			ny =r.getString(R.string.zzzzz_wu21);
		if (dstr.equals("26") || dstr.equals("37"))
			ny = r.getString(R.string.zzzzz_wu22);
		if (dstr.equals("48") || dstr.equals("59"))
			ny = r.getString(R.string.zzzzz_wu23);
		if (dstr.equals("610") || dstr.equals("711"))
			ny = r.getString(R.string.zzzzz_wu24);
		if (dstr.equals("80") || dstr.equals("91"))
			ny = r.getString(R.string.zzzzz_wu25);
		if (dstr.equals("02") || dstr.equals("13"))
			ny = r.getString(R.string.zzzzz_wu26);
		if (dstr.equals("24") || dstr.equals("35"))
			ny = r.getString(R.string.zzzzz_wu27);
		if (dstr.equals("46") || dstr.equals("57"))
			ny = r.getString(R.string.zzzzz_wu28);
		if (dstr.equals("68") || dstr.equals("79"))
			ny = r.getString(R.string.zzzzz_wu29);
		if (dstr.equals("810") || dstr.equals("911"))
			ny = r.getString(R.string.zzzzz_wu30);
		return (ny);
	}

	/**
	 * 获取犯冲的信息
	 * @param d
	 * @param dd
	 * @return
	 */
	public static String CalConv(int d, int dd) {
		Resources r = CalendarData.context.getResources();
		return r.getString(R.string.zzzzz_sha) + sfw[dd] + " " + AnimalIdx[dd] + r.getString(R.string.zzzzz_chong)+"(" + Gan5[d]
				+ Zhi3[dd] + ")" + AnimalIdx2[dd];
	}

	/**
	 * 获取当日大事不宜信息，即日值岁破, 大事不宜; 日值月破, 大事不宜; 日值上朔, 大事不宜; 日值杨公十三忌, 大事不宜
	 * @param yy 年
	 * @param mm 月
	 * @param dd 日
	 * @param y 
	 * @param d
	 * @param m
	 * @param dt
	 * @param nm
	 * @param nd
	 * @return
	 */
	public static String CalConv2(int yy, int mm, int dd, int y, int d, int m,
			int dt, int nm, int nd) {
		String dy = d + "" + dd;
		if ((yy == 0 && dd == 6) || (yy == 6 && dd == 0)
				|| (yy == 1 && dd == 7) || (yy == 7 && dd == 1)
				|| (yy == 2 && dd == 8) || (yy == 8 && dd == 2)
				|| (yy == 3 && dd == 9) || (yy == 9 && dd == 3)
				|| (yy == 4 && dd == 10) || (yy == 10 && dd == 4)
				|| (yy == 5 && dd == 11) || (yy == 11 && dd == 5)) {
			// return "��ֵ���� ���²���";
			return CalendarData.context.getResources()
					.getString(R.string.zzzzz_suiPo);
		} else if ((mm == 0 && dd == 6) || (mm == 6 && dd == 0)
				|| (mm == 1 && dd == 7) || (mm == 7 && dd == 1)
				|| (mm == 2 && dd == 8) || (mm == 8 && dd == 2)
				|| (mm == 3 && dd == 9) || (mm == 9 && dd == 3)
				|| (mm == 4 && dd == 10) || (mm == 10 && dd == 4)
				|| (mm == 5 && dd == 11) || (mm == 11 && dd == 5)) {
			// return "��ֵ���� ���²���";
			return CalendarData.context.getResources()
					.getString(R.string.zzzzz_yuePo);
		} else if ((y == 0 && dy.equals("911")) || (y == 1 && dy.equals("55"))
				|| (y == 2 && dy.equals("111")) || (y == 3 && dy.equals("75"))
				|| (y == 4 && dy.equals("311")) || (y == 5 && dy.equals("95"))
				|| (y == 6 && dy.equals("511")) || (y == 7 && dy.equals("15"))
				|| (y == 8 && dy.equals("711")) || (y == 9 && dy.equals("35"))) {
			return CalendarData.context.getResources().getString(
					R.string.zzzzz_shangsuo);
			// return "��ֵ��˷ ���²���";
		} else if ((m == 1 && dt == 13) || (m == 2 && dt == 11)
				|| (m == 3 && dt == 9) || (m == 4 && dt == 7)
				|| (m == 5 && dt == 5) || (m == 6 && dt == 3)
				|| (m == 7 && dt == 1) || (m == 7 && dt == 29)
				|| (m == 8 && dt == 27) || (m == 9 && dt == 25)
				|| (m == 10 && dt == 23) || (m == 11 && dt == 21)
				|| (m == 12 && dt == 19)) {
			// return "��ֵ�ʮ��� ���²���";
			return CalendarData.context.getResources().getString(
					R.string.zzzzz_yanggong);
		} else {
			return "";
		}
	}

	/**
	 * 获取黑黄道十二吉凶日，即建、除、满、平、定、执、破、 危、成、收、开、闭
	 * @param num
	 * @param num2
	 * @return 黑黄道十二吉凶日之一
	 */
	public static String cyclical6(int num, int num2) {
		if (num == 0)
			return (jcName0[num2]);
		else if (num == 1)
			return (jcName1[num2]);
		else if (num == 2)
			return (jcName2[num2]);
		else if (num == 3)
			return (jcName3[num2]);
		else if (num == 4)
			return (jcName4[num2]);
		else if (num == 5)
			return (jcName5[num2]);
		else if (num == 6)
			return (jcName6[num2]);
		else if (num == 7)
			return (jcName7[num2]);
		else if (num == 8)
			return (jcName8[num2]);
		else if (num == 9)
			return (jcName9[num2]);
		else if (num == 10)
			return (jcName10[num2]);
		else if (num == 11)
			return (jcName11[num2]);
		else
			return "";
	}

	public static String jznyy(String d) {
		String nyy = "";
		if (d.equals("00") || d.equals("11"))
			nyy = "��";
		if (d.equals("22") || d.equals("33"))
			nyy = "��";
		if (d.equals("44") || d.equals("55"))
			nyy = "ľ";
		if (d.equals("66") || d.equals("77"))
			nyy = "��";
		if (d.equals("88") || d.equals("99"))
			nyy = "��";
		if (d.equals("010") || d.equals("111"))
			nyy = "��";
		if (d.equals("20") || d.equals("31"))
			nyy = "ˮ";
		if (d.equals("42") || d.equals("53"))
			nyy = "��";
		if (d.equals("64") || d.equals("75"))
			nyy = "��";
		if (d.equals("86") || d.equals("97"))
			nyy = "ľ";
		if (d.equals("08") || d.equals("19"))
			nyy = "ˮ";
		if (d.equals("210") || d.equals("311"))
			nyy = "��";
		if (d.equals("40") || d.equals("51"))
			nyy = "��";
		if (d.equals("62") || d.equals("73"))
			nyy = "ľ";
		if (d.equals("84") || d.equals("95"))
			nyy = "ˮ";
		if (d.equals("06") || d.equals("17"))
			nyy = "��";
		if (d.equals("28") || d.equals("39"))
			nyy = "��";
		if (d.equals("410") || d.equals("511"))
			nyy = "ľ";
		if (d.equals("60") || d.equals("71"))
			nyy = "��";
		if (d.equals("82") || d.equals("93"))
			nyy = "��";
		if (d.equals("04") || d.equals("15"))
			nyy = "��";
		if (d.equals("26") || d.equals("37"))
			nyy = "ˮ";
		if (d.equals("48") || d.equals("59"))
			nyy = "��";
		if (d.equals("610") || d.equals("711"))
			nyy = "��";
		if (d.equals("80") || d.equals("91"))
			nyy = "ľ";
		if (d.equals("02") || d.equals("13"))
			nyy = "ˮ";
		if (d.equals("24") || d.equals("35"))
			nyy = "��";
		if (d.equals("46") || d.equals("57"))
			nyy = "��";
		if (d.equals("68") || d.equals("79"))
			nyy = "ľ";
		if (d.equals("810") || d.equals("911"))
			nyy = "ˮ";
		return (nyy);
	}

	/**
	 * 获取宜忌信息，以"|"分割，"|"之前是宜，"|"之后是忌
	 */
	public static String jcr(String s) {
		String jcrjx = "";
		if (s.equals(jcName0[0])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS1);
		} else if (s.equals(jcName0[1])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS2);
		} else if (s.equals(jcName0[2])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS3);
		} else if (s.equals(jcName0[3])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS4);
		} else if (s.equals(jcName0[4])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS5);
		} else if (s.equals(jcName0[5])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS6);
		} else if (s.equals(jcName0[6])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS7);
		} else if (s.equals(jcName0[7])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS8);
		} else if (s.equals(jcName0[8])) {
			jcrjx = CalendarData.context.getResources()
					.getString(R.string.zzzzz_jcS9);
		} else if (s.equals(jcName0[9])) {
			jcrjx = CalendarData.context.getResources().getString(
					R.string.zzzzz_jcS10);
		} else if (s.equals(jcName0[10])) {
			jcrjx = CalendarData.context.getResources().getString(
					R.string.zzzzz_jcS11);
		} else if (s.equals(jcName0[11])) {
			jcrjx = CalendarData.context.getResources().getString(
					R.string.zzzzz_jcS12);
		} else
			return "";

		return jcrjx;
	}

	/**
	 * 获取十二神，即青龙，明堂，天刑，朱雀，金匮，天德，白虎，玉堂，天牢，玄武，司命，勾陈
	 * 其中青龍、明堂、金匱、天德、玉堂、司命六神所值的屬於黃道吉日。
　　 * 其他天刑、朱雀、白虎、天牢、玄武、勾陳六神所值的屬於黑道凶日。
	 * @param num
	 * @param num2
	 * @return 返回十二神之一
	 */
	public static String cyclical7(int num, int num2) {
		Resources r = CalendarData.context.getResources();
		if (num == 2)
			return r.getString(zrxName1Id[num2]);
		if (num == 3)
			return r.getString(zrxName2Id[num2]);
		if (num == 4)
			return r.getString(zrxName3Id[num2]);
		if (num == 5)
			return r.getString(zrxName4Id[num2]);
		if (num == 6)
			return r.getString(zrxName5Id[num2]);
		if (num == 7)
			return r.getString(zrxName6Id[num2]);
		if (num == 8)
			return r.getString(zrxName7Id[num2]);
		if (num == 9)
			return r.getString(zrxName8Id[num2]);
		if (num == 10)
			return r.getString(zrxName9Id[num2]);
		if (num == 11)
			return r.getString(zrxName10Id[num2]);
		if (num == 0)
			return r.getString(zrxName11Id[num2]);
		if (num == 1)
			return r.getString(zrxName12Id[num2]);
		else
			return "";
	}

	/**
	 * 获取农历一天的字符串，如初一，初二，初三，初四，初五...初十，十一...二十...三十
	 * @param d 日期在月份的索引
	 * @return
	 */
	public static String getLunarDay(int d) {
		String s = "";
		Resources r = CalendarData.context.getResources();
		switch (d) {
		case 10:
			s = r.getString(R.string.zzzzz_chu) + r.getString(R.string.zzzzz_ten);
			break;
		case 20:
			s = r.getString(R.string.zzzzz_two) + r.getString(R.string.zzzzz_ten);
			break;
		case 30:
			s = r.getString(R.string.zzzzz_three) + r.getString(R.string.zzzzz_ten);
			break;
		default:
			s = nStr2[(int) Math.floor(d / 10)];
			s += CalendarData.nStr1[(d % 10)];
		}
		return (s);
	}

	/**
	 * 返回农历月份的称呼，如正月，腊月。。
	 * @param res
	 * @param m
	 * @return
	 */
	public static String getLunarMonth(Resources res, int m) {

		String s2 = "";
		switch (m) {
		case 1:
			s2 = res.getString(
					R.string.zzzzz_lunarOne);
			break;
		case 2:
			s2 = CalendarData.nStr1[2];
			break;
		case 3:
			s2 = CalendarData.nStr1[3];
			break;
		case 4:
			s2 = CalendarData.nStr1[4];
			break;
		case 5:
			s2 = CalendarData.nStr1[5];
			break;
		case 6:
			s2 = CalendarData.nStr1[6];
			break;
		case 7:
			s2 = CalendarData.nStr1[7];
			break;
		case 8:
			s2 = CalendarData.nStr1[8];
			break;
		case 9:
			s2 = CalendarData.nStr1[9];
			break;
		case 10:
			s2 = CalendarData.nStr1[10];
			break;
		case 11:
			s2 = res.getString(R.string.zzzzz_dong);
			break;
		case 12:
			s2 =res.getString(R.string.zzzzz_na);
			break;
		default:
		}
		return (s2);
	}

	public static void init(Context context) {
		CalendarData.context = context;
		Resources r = context.getResources();
		
		CalendarData.nStr1 = new String[CalendarData.nStr1Id.length];
		for (int i = 0; i < CalendarData.nStr1.length; i++) {
			CalendarData.nStr1[i] = CalendarData.context.getResources().getString(CalendarData.nStr1Id[i]);
		}
		
		nStr2 = new String[nStr2Id.length];

		for (int i = 0; i < nStr2.length; i++) {
			nStr2[i] = r.getString(nStr2Id[i]);
		}

		// 初始化阳历节日数据, 如元旦，劳动节，情人节，国庆节等
		sFtv = new String[sFtvId.length];
		for (int i = 0; i < sFtv.length; i++) {
			sFtv[i] = r.getString(sFtvId[i]);
		}

		// 初始化农历节日数据，如春节，元宵节，寒食节，端午节等
		lFtv = new String[lFtvId.length];
		for (int i = 0; i < lFtv.length; i++) {
			lFtv[i] = r.getString(lFtvId[i]);
		}

		// 初始化世界节日，如母亲节，父亲节，合作节，被奴役国家周等
		wFtv = new String[wFtvId.length];
		for (int i = 0; i < wFtv.length; i++) {
			wFtv[i] = r.getString(wFtvId[i]);
		}

		// 初始化二十节气，如小寒，大寒，立春，雨水，惊蛰等
		solarTerm = new String[sTermId.length];
		for (int i = 0; i < solarTerm.length; i++) {
			solarTerm[i] = r.getString(sTermId[i]);
		}
		
		// 初始化宜忌数据
		jcName0 = new String[jcName0Id.length];
		jcName1 = new String[jcName0Id.length];
		jcName2 = new String[jcName0Id.length];
		jcName3 = new String[jcName0Id.length];
		jcName4 = new String[jcName0Id.length];
		jcName5 = new String[jcName0Id.length];
		jcName6 = new String[jcName0Id.length];
		jcName7 = new String[jcName0Id.length];
		jcName8 = new String[jcName0Id.length];
		jcName9 = new String[jcName0Id.length];
		jcName10 = new String[jcName0Id.length];
		jcName11 = new String[jcName0Id.length];
		for (int i = 0; i < jcName0.length; i++) {
			jcName0[i] = r.getString(jcName0Id[i]);
			jcName1[i] = r.getString(jcName1Id[i]);
			jcName2[i] = r.getString(jcName2Id[i]);
			jcName3[i] = r.getString(jcName3Id[i]);
			jcName4[i] = r.getString(jcName4Id[i]);
			jcName5[i] = r.getString(jcName5Id[i]);
			jcName6[i] = r.getString(jcName6Id[i]);
			jcName7[i] = r.getString(jcName7Id[i]);
			jcName8[i] = r.getString(jcName8Id[i]);
			jcName9[i] = r.getString(jcName9Id[i]);
			jcName10[i] = r.getString(jcName10Id[i]);
			jcName11[i] = r.getString(jcName11Id[i]);
		}
		
		// 十天干数据，如甲、乙、丙、丁、戊、己、庚、辛、壬、癸
		GAN = new String[GanId.length];
		// 天干日不宜数据，如甲不开仓、乙不栽植、丙不修灶、丁不剃头等
		Gan2 = new String[Gan2Id.length];
		// 天干地支数据，如：
		// 甲子 乙丑 丙寅 丁卯 戊辰 己巳 庚午 辛未 壬申 癸酉 甲戌 乙亥
		// 丙子 丁丑 戊寅 己卯 庚辰 辛巳 壬午 癸未 甲申 乙酉 丙戌 丁亥
		// ...
		Gan3 = new String[Gan3Id.length];
		Gan5 = new String[Gan5Id.length];
		for (int i = 0; i < GAN.length; i++) {
			GAN[i] = r.getString(GanId[i]);
			Gan5[i] = r.getString(Gan5Id[i]);
			Gan2[i] = r.getString(Gan2Id[i]);
			Gan3[i] = r.getString(Gan3Id[i]);
		}
		
		// 十二地支数据，如子、丑、寅、卯、辰、巳、午、未、申、酉、戌、亥
		ZHI = new String[Zhi0Id.length];
		Zhi2 = new String[Zhi0Id.length];
		// 地支时段不宜数据，如子不问卜、丑不冠带、寅不祭祀、卯不穿井等
		Zhi3 = new String[Zhi3Id.length];
		Zhi = new String[ZhiId.length];
		for (int i = 0; i < ZHI.length; i++) {
			ZHI[i] = r.getString(Zhi0Id[i]);
			Zhi[i] = r.getString(Zhi0Id[i]);
			Zhi2[i] = r.getString(ZhiId[i]);
			Zhi3[i] = r.getString(Zhi3Id[i]);
		}
		
		// 十二生肖，如鼠、牛、虎、兔、蛇、马、羊、猴、鸡、狗、猪
	   AnimalIdx = new String[12];
	   AnimalIdx2 = new String[12];
	   for(int i=0; i<12; i++){
		   AnimalIdx[i]=r.getString(AnimalId[i]);
		   AnimalIdx2[i]=r.getString(AnimalId2[i]);
	   }
	   // 南北东西数据，如南、北、东、西
	   sfw = new String[12];
	   for(int i=0;i<12;i++){
		   sfw[i]=r.getString(sfwId[i]);
	   }
	   // 星座信息
	   mConstellations = new String[mConstellationIds.length];
	   for(int i = 0; i < mConstellationIds.length; i++){
		   mConstellations[i] = r.getString(mConstellationIds[i]);
	   }
	}
	
	/**
	 * 获取星座信息
	 * @param solarMonth 阳历月
	 * @param solarDay   阳历日
	 * @return 星座信息 如1月20日~2月18日-水瓶座
	 */
	public static String getConstellation(int solarMonth, int solarDay){
		int index = -1;
		switch(solarMonth){
		case 1:
			if(solarDay <= 19 && solarDay >= 1){
				index = 11;
			}else if(solarDay >=20 && solarDay <= 31){
				index = 0;
			}
			break;
		case 2:
			if(solarDay <= 18 && solarDay >= 1){
				index = 0;
			}else if(solarDay >=19 && (solarDay <= 28 || solarDay <= 29)) {
				index = 1;
			}
			break;
		case 3:
			if(solarDay <= 20 && solarDay >= 1){
				index = 1;
			}else if(solarDay >=21 && solarDay <= 31){
				index = 2;
			}
			break;
		case 4:
			if(solarDay <= 19 && solarDay >= 1){
				index = 2;
			}else if(solarDay >=20 && solarDay <= 30) {
				index = 3;
			}
			break;
		case 5:
			if(solarDay <= 20 && solarDay >= 1){
				index = 3;
			}else if(solarDay >=21 && solarDay <= 31){
				index = 4;
			}
			break;
		case 6:
			if(solarDay <= 21 && solarDay >= 1){
				index = 4;
			}else if(solarDay >=22 && solarDay <= 30){
				index = 5;
			}
			break;
		case 7:
			if(solarDay <= 22 && solarDay >= 1){
				index = 5;
			}else if(solarDay >=23 && solarDay <= 31){
				index = 6;
			}
			break;
		case 8:
			if(solarDay <= 22 && solarDay >= 1){
				index = 6;
			}else if(solarDay >=23 && solarDay <= 31){
				index = 7;
			}
			break;
		case 9:
			if(solarDay <= 22 && solarDay >= 1){
				index = 7;
			}else if(solarDay >=23 && solarDay <= 30){
				index = 8;
			}
			break;
		case 10:
			if(solarDay <= 23 && solarDay >= 1){
				index = 8;
			}else if(solarDay >=24 && solarDay <= 31){
				index = 9;
			}
			break;
		case 11:
			if(solarDay <= 22 && solarDay >= 1){
				index = 9;
			}else if(solarDay >=23 && solarDay <= 30){
				index = 10;
			}
			break;
		case 12:
			if(solarDay <= 21 && solarDay >= 1){
				index = 10;
			}else if(solarDay >=22 && solarDay <= 31){
				index = 11;
			}
			break;
		}
		if(index != -1){
			return mConstellations[index];
		}
		return null;
	}

	/**
	 * 判断年月是否支持
	 * 
	 * @param activity
	 * @param year
	 * @param month
	 * @return
	 */
	public static boolean isSupportedYearMonth(Activity activity, int year, int month) {
		if (year < 1900 || (year == 1900 && month < 2)) {
			Toast.makeText(activity, activity.getResources().getString(R.string.zzzzz_minSupportTime), Toast.LENGTH_SHORT).show();
			return false;
		} else if (year > 2100 || (year == 2100 && month > 11)) {
			Toast.makeText(activity, activity.getResources().getString(R.string.zzzzz_maxSupportTime), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		p(lYearDays(1990) + "");
	}
}
