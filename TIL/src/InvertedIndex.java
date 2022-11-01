package ssafy;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
public class InvertedIndex {
    /*
    Object, Text : input key-value pair type (always same (to get a line of input file))
    Text, IntWritable : output key-value pair type
    */
    public static class TokenizerMapper
            extends Mapper<Object,Text,Text,Text> {

        // variable declairations
        private Text word = new Text(); // key로 사용할 변수
        private Text pos = new Text(); // value로 사용할 변수
        private String filename; // 현재 읽는 파일 이름을 가져올 변수

        @Override
        protected void setup(Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            //현재 사용하는 파일 이름을 뽑아서 저장
            filename = ((FileSplit)context.getInputSplit()).getPath().getName();
        }

        // map function (Context -> fixed parameter)
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException { //입력 파일을 한 줄 읽고
            // value.toString() : get a line
            StringTokenizer itr = new StringTokenizer(value.toString()," ",true); // 단어 단위로 자르기
            long p = ((LongWritable)key).get(); // 현재 위치가 시작 지점에서 몇 bytes인가 표시
            while ( itr.hasMoreTokens() ) { // 다음 단어가 없을 때까지 계속 수행
                String token = itr.nextToken(); // 다음 단어 가져오기
                word.set(token.trim());
                if(!token.equals(" ")) {
                    pos.set(filename+":"+p);
                    context.write(word,pos);
                }
                p+= token.length(); //현재 위치에 단어길이 + delimiter를 더함
            }
        }
    }
    /*
        Text, IntWritable : input key type and the value type of input value list
        Text, IntWritable : output key-value pair type
        */
    public static class ConcatenatorReducer
            extends Reducer<Text,Text,Text,Text> {

        // variables
        private Text list = new Text();

        // key : a disticnt word
        // values :  Iterable type (data list)
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            String s = new String();
            int comma = 0;
            for(Text val : values) {
                if(comma == 0) {
                    comma = 1;
                    s += (":"+ val.toString());
                }
                else
                    s += (",    " + val.toString());
            }
            list.set(s);
            context.write(key,list);
        }
    }

    /* Main function */
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs(); //hadoop jar jarname.jar 프로그램명을 제외한 변수 저장
        if ( otherArgs.length != 2 ) {
            System.err.println("Usage: <in> <out>");
            System.exit(2);
        }
        FileSystem hdfs = FileSystem.get(conf);
        Path output = new Path(otherArgs[1]);
        if(hdfs.exists(output))
            hdfs.delete(output,true);
        Job job = new Job(conf,"inverted index");
        job.setJarByClass(InvertedIndex.class);

        // let hadoop know my map and reduce classes
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(ConcatenatorReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // set number of reduces
        job.setNumReduceTasks(2);

        // set input and output directories
        FileInputFormat.addInputPath(job,new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
        FileSystem.get(job.getConfiguration()).delete(new Path(otherArgs[1]),true);
        System.exit(job.waitForCompletion(true) ? 0 : 1 );
    }
}



