/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

/* 
 
 Demo of the background/foreground detection algorithm.
 Found somewhere on the old OpenCV forums.
 
 */

#include <OpenCV/OpenCV.h>
#include <ctype.h>
#include <stdio.h>

int main(int argc, char** argv)
{
	
    /* Start capturing */
    CvCapture* capture = 0;
	
    if( argc == 1 || (argc == 2 && strlen(argv[1]) == 1 && isdigit(argv[1][0])))
        capture = cvCaptureFromCAM( argc == 2 ? argv[1][0] - '0' : 0 );
    else if( argc == 2 )
        capture = cvCaptureFromAVI( argv[1] );
	
    if( !capture )
    {
        fprintf(stderr,"Could not initialize...\n");
        return -1;
    }
	
    /* print a welcome message, and the OpenCV version */
    printf ("Demo of the background classification using CvGaussBGModel %s (%d.%d.%d)\n",
			CV_VERSION,
			CV_MAJOR_VERSION, CV_MINOR_VERSION, CV_SUBMINOR_VERSION);
	
    /* Capture 1 video frame for initialization */
    IplImage* videoFrame = NULL;
    videoFrame = cvQueryFrame(capture);
	
    if(!videoFrame)
    {
        printf("Bad frame \n");
        exit(0);
    }
	
    // Create windows
    cvNamedWindow("BG", 1);
    cvNamedWindow("FG", 1);
	
    // Select parameters for Gaussian model.
    CvGaussBGStatModelParams* params = new CvGaussBGStatModelParams;						
    params->win_size=2;	
    params->n_gauss=3;
    params->bg_threshold=0.25;
    params->std_threshold=4.5;
    params->minArea=25;
    params->weight_init=0.025;
    params->variance_init=30; 
	
    // Creat CvBGStatModel
    // cvCreateGaussianBGModel( IplImage* first_frame, CvGaussBGStatModelParams* parameters )
    // or
    // cvCreateGaussianBGModel( IplImage* first_frame )
    CvBGStatModel* bgModel = cvCreateGaussianBGModel(videoFrame ,params);
	
    int key=-1;
    while(key != 'q')
    {
        // Grab a fram
        videoFrame = cvQueryFrame(capture);
        if( !videoFrame )
            break;
        
        // Update model
        cvUpdateBGStatModel(videoFrame,bgModel);
        
        // Display results
       	cvShowImage("BG", bgModel->background);
       	cvShowImage("FG", bgModel->foreground);	
       	key = cvWaitKey(10);
    }
	
    cvDestroyWindow("BG");
    cvDestroyWindow("FG");
    cvReleaseBGStatModel( &bgModel );
    cvReleaseCapture(&capture);
    return 0;
}

