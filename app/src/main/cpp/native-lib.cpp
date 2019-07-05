#include <jni.h>
#include <string>
#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include <android/log.h>
static const char *TAG = "Zachary";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

extern "C"
JNIEXPORT jobject JNICALL
Java_com_adyun_serialport_SerialPortManager_open(JNIEnv *env, jobject instance, jstring path_,
                                                 jint baudRate) {
    const char *path = env->GetStringUTFChars(path_, 0);
    jobject mFileDescriptor;
    int  fd = open(path,O_RDWR);
    if(fd == -1){
        ("打开失败");
        return 0;
    }
    struct termios cfg;
    LOGE("------------------1");
    if (tcgetattr(fd,&cfg)){
        close(fd);
        return 0;

    }
    // 设置串口读取的波特率
    cfmakeraw(&cfg);
    cfsetispeed(&cfg,B115200);
    //    设置串口写入波特率
    cfsetospeed(&cfg, B115200);

    if(tcsetattr(fd,TCSANOW,&cfg)){
        close(fd);
        return 0;
    }
    jclass cFileDescriptor = env->FindClass( "java/io/FileDescriptor");
    jmethodID iFileDescriptor = env->GetMethodID( cFileDescriptor, "<init>", "()V");
    jfieldID descriptorID = env->GetFieldID(cFileDescriptor, "descriptor", "I");
    mFileDescriptor = env->NewObject(cFileDescriptor, iFileDescriptor);
    env->SetIntField( mFileDescriptor, descriptorID, (jint)fd);
    return mFileDescriptor;

}