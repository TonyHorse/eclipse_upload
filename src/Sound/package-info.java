/**
 * 
 */
/**
 * @author bin_hu
 *
 * 2018年5月31日
 */
package Sound;

/*
 * 一些播放音乐有关的类:
 * Mp3Player.java：
 * 	1）一个基于JMF编写的用于播放各种背景音乐的类
 * 	名义上写的是Mp3Player，但实际上由于JMF播放格式的局限性，
 * 	此处并不能直接播放MP3格式，只能播放WAV格式(可以将MP3
 * 	转换换成WAV进行播放)
 * 	2）Mp3Player通过一个阻塞标志来控制其是否播放
 * SoundResource.java：顾名思义，就是各种各样的音乐资源，
 * 	想要使用的音乐都可以在此处实例化为静态Mp3Player成员变量
 * 	在后面要用到的地方直接调用即可
 */