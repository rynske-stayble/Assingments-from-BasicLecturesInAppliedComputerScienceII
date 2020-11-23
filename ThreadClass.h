#ifndef _THREAD_HEADER
#define _THREAD_HEADER

#include <windows.h>
#include <process.h>    /* _beginthread, _endthread */
#include <stddef.h>
#include <stdlib.h>
#include <conio.h>

static unsigned int _stdcall TheadFunction(void*);

class THREAD
{
	friend unsigned int _stdcall ThreadFunction(void*);
public:
	THREAD(void);
	virtual ~THREAD(void);
	int Start(void* = NULL);
	void Detach(void);
	void* Wait(void);
	void Stop(void);
	unsigned int GetThreadID(void);
	static unsigned int GetCurrentThreadID(void);
	static void Sleep(int);
	void Reset(void);
protected:
	virtual void* Run(void*){ return NULL;}
	private:
	HANDLE ThreadHandle;
	unsigned int ThreadID;
	BOOL Started;
	BOOL Detached;
	void* Param;
};

class MUTEX
{
public:
	MUTEX(void);
	MUTEX(HANDLE*);
	virtual ~MUTEX(void);
	virtual void Acquire(void);
	virtual int Acquired(void);
	virtual void Release(void);
private:
	HANDLE* Mutex;
	BOOL dyna;
};

class EVENT
{
public:
	EVENT(int = TRUE);
	~EVENT(void);
	void Signal(void);
	void Wait(void);
	void Reset(void);
	int Test(void);
private:
	HANDLE Event;
};

const int SEMAPHORE_MAX_COUNT = 10000;

class SEMAPHORE 
{
public:
	SEMAPHORE(int = 0);
	~SEMAPHORE(void);
	void Signal(void);
	void Wait(void);
	int Test(void);
private:
	HANDLE Semaphore;
};

#endif

