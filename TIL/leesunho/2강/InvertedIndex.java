package ssafy;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.GenericOptionsParser;

public class InvertedIndex {
	/* 
	Object, Text : input key-value pair type (always same (to get a line of input file))
	Text, IntWritable : output key-value pair type
	*/
	public static class TokenizerMapper
			extends Mapper<Object,Text,Text,Text> {

		// variable declairations
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		//get a fileName!!!
		private String filename;
		protected void setup(Context context) throws IOException, InterruptedException{
		
			filename=((FileSplit)context.getInputSplit()).getPath().getName();
		}

		// map function (Context -> fixed parameter)
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			setup(context);
			// value.toString() : get a line
			StringTokenizer itr = new StringTokenizer(value.toString()," ",true);
			
			long leng=((LongWritable)key).get();
			
			while ( itr.hasMoreTokens() ) {
			
				String w = itr.nextToken();
				word.set(w);
				
				Text val = new Text();
				val.set(filename+":"+leng);
				// emit a key-value pair
				context.write(word,val);
				leng+= w.length();
			}
		}
		
		
	}

	/*
	Text, IntWritable : input key type and the value type of input value list
	Text, IntWritable : output key-value pair type
	*/
	public static class IntSumReducer
			extends Reducer<Text,IntWritable,Text,Text> {

		// variables
		private Text result = new Text();

		// key : a disticnt word
		// values :  Iterable type (data list)
		public void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException {

			int sum = 0;
			String res="";
			for ( Text val : values ) {
				res += val.toString();
			}
			result.set(res);
			context.write(key,result);
		}
	}


	/* Main function */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
		if ( otherArgs.length != 2 ) {
			System.err.println("Usage: <in> <out>");
			System.exit(2);
		}
		
		FileSystem hdfs = FileSystem.get(conf);
		Path output = new Path(otherArgs[1]);
		if(hdfs.exists(output))
			hdfs.delete(output, true);
		
		
		Job job = new Job(conf,"word count sort");
		job.setJarByClass(InvertedIndex.class);

		// let hadoop know my map and reduce classes
		job.setMapperClass(TokenizerMapper.class);
		job.setReducerClass(IntSumReducer.class);
		

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// set number of reduces
		job.setNumReduceTasks(2);

		// set input and output directories
		FileInputFormat.addInputPath(job,new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1 );
	}
}

